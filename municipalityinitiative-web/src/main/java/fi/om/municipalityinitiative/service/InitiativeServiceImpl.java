package fi.om.municipalityinitiative.service;

import static fi.om.municipalityinitiative.dto.EditMode.CURRENT_AUTHOR;
import static fi.om.municipalityinitiative.dto.EditMode.FULL;
import static fi.om.municipalityinitiative.util.Locales.FI;
import static fi.om.municipalityinitiative.util.Locales.SV;

import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;
import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;

import com.google.common.collect.Sets;
import com.mysema.commons.lang.Assert;

import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dao.NotFoundException;
import fi.om.municipalityinitiative.dto.*;
import fi.om.municipalityinitiative.validation.AuthorRoleValidator;
import fi.om.municipalityinitiative.validation.LocalizationRequiredValidator;
import fi.om.municipalityinitiative.validation.group.VRK;

public class InitiativeServiceImpl implements InitiativeService {

    private final Logger log = LoggerFactory.getLogger(InitiativeServiceImpl.class); 
    
    @Resource InitiativeDao initiativeDao;
    
    @Resource UserService userService;

    @Resource EmailService emailService;
    
    @Resource SmartValidator validator;
    
    @Resource EncryptionService encryptionService;

    @Resource InitiativeSettings initiativeSettings;
    
    private final int invitationCodeLength = 12; // Multiples of 3 work best
    
    public InitiativeServiceImpl() {
    }
    
    public InitiativeServiceImpl(InitiativeDao initiativeDao, UserService userService, 
                                 EmailService emailService, EncryptionService encryptionService, 
                                 SmartValidator validator,
                                 InitiativeSettings initiativeSettings) {
        this.initiativeDao = initiativeDao;
        this.userService = userService;
        this.emailService = emailService;
        this.encryptionService = encryptionService;
        this.validator = validator;
        this.initiativeSettings = initiativeSettings;
    }
    
    @Override
    @Transactional(readOnly=true)
    public InitiativePublic getInitiativeForPublic(Long initiativeId) {
        InitiativePublic initiative = initiativeDao.getInitiativeForPublic(initiativeId);
        if (initiative == null) {
            throw new NotFoundException("initiative", initiativeId);
        } else {
            if (initiative.getState() == InitiativeState.DRAFT) {
                User user = userService.getUserInRole(Role.REGISTERED);
                // Allow preview for authors even if it's not implemented yet
                if (getAuthor(initiativeId, user.getId()) == null) {
                    throw new AccessDeniedException("No public view for a draft.");
                }
            }
            
            return initiative;
        }
    }

    @Override
    @Transactional(readOnly=true)
    public InitiativeManagement getInitiativeForManagement(Long initiativeId) {
        return getInitiativeForManagement(initiativeId, false);
    }

    private InitiativeManagement getInitiativeForManagement(Long initiativeId, boolean forUpdate) {
        // NOTE: User must be REGISTERED before allowed to manage existing initiative
        User user = userService.getUserInRole(Role.REGISTERED);
        
        InitiativeManagement initiative = initiativeDao.getInitiativeForManagement(initiativeId, forUpdate);
        if (initiative == null) {
            throw new NotFoundException("initiative", initiativeId);
        } else {
            Author author = requireAuthorOrOfficial(initiativeId, user);
            initiative.setCurrentAuthor(author);
            return initiative;
        }
    }
    
    private Author requireAuthorOrOfficial(Long initiativeId, User user) {
        Author author = getAuthor(initiativeId, user.getId());

        if (author != null) {
            return author;
        } else if (user.isOm() || user.isVrk()) {
            return null;
        } else {
            throw new AccessDeniedException("Not an author or om or vrk official.");
        }
    }

    @Override
    @Transactional(readOnly=false)
    public InitiativeManagement update(InitiativeManagement initiative, EditMode editMode, Errors errors) {
        final String METHOD_NAME = "update";
        // NOTE: User MUST be registered before allowed to update initiative
        User user = userService.getUserInRole(Role.REGISTERED);

        Assert.notNull(initiative, "initiative");
        Assert.notNull(initiative.getId(), "initiative.id");
        Assert.notNull(editMode, "editMode");
        Assert.notNull(errors, "errors");
        
        if (editMode == EditMode.NONE) {
            return initiative;
        }

        // TODO: OM and VRK users are allowed to edit keywords and links
        InitiativeManagement persisted = getInitiativeForManagement(initiative.getId(), true);
        ManagementSettings managementSettings = initiativeSettings.getManagementSettings(persisted, editMode, user);
        
        initiative = mergeEdits(initiative, persisted, editMode, managementSettings);
        
        Author editAuthor = initiative.getCurrentAuthor();
        if (editAuthor != null) {
            assignAuthorAutoFields(editAuthor, user);
        }
        
        if (validate(initiative, user, errors, editMode.getValidationGroups())) {
            boolean updateBasic = false;
            boolean updateExtra = false;
            
            switch (editMode) {
            case FULL:
            case BASIC:
                require(managementSettings.isAllowEditBasic());
                updateBasic = true;
                initiativeDao.clearConfirmations(initiative.getId(), user.getId());
                if (editMode != FULL) break;
            case EXTRA:
                require(managementSettings.isAllowEditExtra());
                updateExtra = true;
                initiativeDao.updateLinks(initiative.getId(), initiative.getLinks());
                if (editMode != FULL) break;
            case ORGANIZERS:
                require(managementSettings.isAllowEditOrganizers());
                initiativeDao.updateInvitations(initiative.getId(), 
                        initiative.getInitiatorInvitations(), 
                        initiative.getRepresentativeInvitations(), 
                        initiative.getReserveInvitations());
                if (editMode != FULL) break;
            case CURRENT_AUTHOR:
                require(managementSettings.isAllowEditCurrentAuthor());
                initiativeDao.updateAuthor(initiative.getId(), user.getId(), editAuthor);
                //if (editMode != FULL) break;
                break;
            default: 
                throw new IllegalArgumentException("Unsupported EditMode: " + editMode);
            }
            // At the minimum this sets the modifierId and modified timestamp (by trigger)
            initiativeDao.updateInitiative(initiative, user.getId(), updateBasic, updateExtra);
            
            log(METHOD_NAME, initiative.getId(), user, true);
            return initiative;
        } else {
            log(METHOD_NAME, initiative.getId(), user, false);
            return initiative;
        }
    }
    
    private void require(boolean right) {
        if (!right) {
            throw new AccessDeniedException();
        }
    }
    
    private boolean validate(Author author, User currentUser, Errors errors) {
        AuthorRoleValidator.setCurrentUser(currentUser);
        try {
            validator.validate(author, errors, (Object[]) CURRENT_AUTHOR.getValidationGroups());
        } finally {
            AuthorRoleValidator.clearCurrentUser(); // Defensive
        }
        return !errors.hasErrors();
    }
    
    private boolean validate(InitiativeManagement initiative, User currentUser, Errors errors, Class<?>... groupClasses) {
        initiative.cleanupAfterBinding(); // if validation fails, empty links will mess the form

        LocalizationRequiredValidator.setRequiredLocales(getRequiredLocales(initiative));
        AuthorRoleValidator.setCurrentUser(currentUser);
        try {
            validator.validate(initiative, errors, (Object[]) groupClasses);
        } finally {
            LocalizationRequiredValidator.clearRequiredLocales(); // Defensive
            AuthorRoleValidator.clearCurrentUser(); // Defensive
        }
        return !errors.hasErrors();
    }
    
    private Set<String> getRequiredLocales(InitiativeManagement initiative) {
        Set<String> requiredLocales = Sets.newHashSet();
        
        if (initiative.hasTranslation(FI)) {
            requiredLocales.add(FI);
        }
        
        if (initiative.hasTranslation(SV)) {
            requiredLocales.add(SV);
        }
        
        if (requiredLocales.isEmpty()) {
            requiredLocales.add(LocaleContextHolder.getLocale().getLanguage());
        }

        return requiredLocales;
    }

    /* (non-Javadoc)
     * @see fi.om.initiative.service.InitiativeService#create(fi.om.initiative.dto.InitiativeDto)
     */
    @Override
    @Transactional(readOnly=false)
    public Long create(InitiativeManagement initiative, Errors errors) {
        final String METHOD_NAME = "create";
        // Requires authenticated user and registers user if he/she is not yet registered
        User user = userService.getUserInRole(Role.AUTHENTICATED);

        Assert.notNull(initiative, "initiative");
        Assert.notNull(errors, "errors");
        Author editAuthor = initiative.getCurrentAuthor();
        Assert.notNull(editAuthor, "author");

        assignAuthorAutoFields(editAuthor, user);
        
        if (validate(initiative, user, errors, FULL.getValidationGroups())) {
            user = userService.currentAsRegisteredUser(); 
            
            initiative.assignEndDate(initiative.getStartDate(), initiativeSettings.getVotingDuration());
            Long id = initiativeDao.create(initiative, user.getId());

            initiativeDao.updateLinks(id, initiative.getLinks());
            
            initiativeDao.updateInvitations(id, initiative.getInitiatorInvitations(), 
                    initiative.getRepresentativeInvitations(), 
                    initiative.getReserveInvitations());
            
            initiativeDao.insertAuthor(id, user.getId(), editAuthor);
            
            log(METHOD_NAME, id, user, true);
            return id;
        } else {
            log(METHOD_NAME, null, user, false);
            return null;
        }
    }
    
    private void assignAuthorAutoFields(Author editAuthor, User user) {
        editAuthor.assignFirstNames(user.getFirstNames());
        editAuthor.assignLastName(user.getLastName());
        editAuthor.assignHomeMunicipality(user.getHomeMunicipality());
    }
    
    private InitiativeManagement mergeEdits(InitiativeManagement edited, InitiativeManagement persisted, EditMode editMode, ManagementSettings managementSettings) {
        switch (editMode) {
        case FULL:
        case BASIC:
            persisted.setName(edited.getName());
            persisted.setStartDate(edited.getStartDate());
            persisted.assignEndDate(edited.getStartDate(), initiativeSettings.getVotingDuration());
            persisted.setProposalType(edited.getProposalType());
            persisted.setProposal(edited.getProposal());
            persisted.setRationale(edited.getRationale());
            persisted.setPrimaryLanguage(edited.getPrimaryLanguage());
            if (editMode != FULL) break;
        case EXTRA:
            // Keywords
            persisted.setFinancialSupport(edited.isFinancialSupport());
            persisted.setFinancialSupportURL(edited.getFinancialSupportURL());
            persisted.setSupportStatementsInWeb(edited.isSupportStatementsInWeb());
            persisted.setSupportStatementsOnPaper(edited.isSupportStatementsOnPaper());
            persisted.setExternalSupportCount(edited.getExternalSupportCount());
            persisted.setLinks(edited.getLinks());
            if (editMode != FULL) break;
        case ORGANIZERS:
            persisted.setInitiatorInvitations(edited.getInitiatorInvitations());
            persisted.setRepresentativeInvitations(edited.getRepresentativeInvitations());
            persisted.setReserveInvitations(edited.getReserveInvitations());
            if (editMode != FULL) break;
        case CURRENT_AUTHOR:
            Author editedAuthor = edited.getCurrentAuthor();
            Author persistedAuthor = persisted.getCurrentAuthor();
            if (editedAuthor != null && persistedAuthor != null) {
                persistedAuthor.setContactInfo(editedAuthor.getContactInfo());
                if (managementSettings.isAllowEditOrganizers()) {
                    persistedAuthor.setInitiator(editedAuthor.isInitiator());
                    persistedAuthor.setRepresentative(editedAuthor.isRepresentative());
                    persistedAuthor.setReserve(editedAuthor.isReserve());
                }
            }
            break;
            //if (editMode != FULL) break;
        }
        
        return persisted;
    }


    @Override
    @Transactional(readOnly=true)
    public List<InitiativeInfo> findInitiatives(InitiativeSearch search) {
        final Long userId;
        if (search.isIncludeOwn()) {
            userId = userService.getUserInRole(Role.REGISTERED).getId();
        } else {
            userId = null;
        }
        return initiativeDao.findInitiatives(search, userId, initiativeSettings.getMinSupportCountForSearch());
    }

    @Override
    @Transactional(readOnly=true)
    public List<InitiativeInfo> findInitiativesWithUnremovedVotes(Period beforeDeadLine) {
        userService.getUserInRole(Role.OM);
        List<InitiativeInfo> initiatives = initiativeDao.findInitiativesWithUnremovedVotes();
        for (int i = initiatives.size() - 1; i >= 0; i--) {
            InitiativeInfo initiative = initiatives.get(i);
            if (!initiative.isVotesRemovalEndDateNear(LocalDate.now(), initiativeSettings.getVotesRemovalDuration(), beforeDeadLine)) {
                initiatives.remove(i);
            }
        }
        return initiatives;
    }
    
    @Override
    @Transactional(readOnly=true)
    public Author getAuthor(Long initiativeId, Long userId) {
        return initiativeDao.getAuthor(initiativeId, userId);
    }

    @Override
    @Transactional(readOnly=false)
    public boolean sendInvitations(Long initiativeId) {
        final String METHOD_NAME = "sendInvitations";
        User user = userService.getUserInRole(Role.REGISTERED);
        
        Author currentAuthor = getAuthor(initiativeId, user.getId());

        if (currentAuthor == null) {
            throw new AccessDeniedException("user is not an author");
        }
        
        InitiativeManagement initiative = getInitiativeForManagement(initiativeId, true);
        ManagementSettings managementSettings = initiativeSettings.getManagementSettings(initiative, user);
        
        if (managementSettings.isAllowSendInvitations()) {
            initiativeDao.updateInitiativeState(initiative.getId(), user.getId(), InitiativeState.PROPOSAL, null);
    
            for (Invitation invitation : initiative.getInvitations()) {
                if (invitation.getSent() == null) { // send only unsent invitations
                    // Use invitation.id to ensure uniqueness! 
                    String invitationCode = invitation.getId() + encryptionService.randomToken(invitationCodeLength);
                    invitation.assignInvitationCode(invitationCode);
    
                    emailService.sendInvitation(initiative, invitation);                    
                    initiativeDao.updateInvitationSent(initiative.getId(), invitation.getId(), invitationCode);
                }
            }
            
            // Confirmation requests
            for (Author author : initiative.getAuthors()) {
                if (author.isRequiresConfirmationReminder()) {
                    emailService.sendConfirmationRequest(initiative, author);
                    initiativeDao.updateConfirmationRequestSent(initiativeId, author.getUserId());
                }
            }
            
            if (initiative.getInvitations().size() > 0) {           //TODO: When this should be sent???
                emailService.sendInvitationSummary(initiative);
            }
            
            log(METHOD_NAME, initiativeId, user, true);
            return true;
        } else {
            log(METHOD_NAME, initiativeId, user, false);
            return false;
        }
    }
    
    @Override
    @Transactional(readOnly=true)
    public @Nullable Invitation getInvitation(Long initiativeId, String invitationCode) {
        Assert.notNull(initiativeId, "initiativeId");
        Assert.hasText(invitationCode, "invitationCode");
        
        return initiativeDao.getOpenInvitation(initiativeId, invitationCode, initiativeSettings.getInvitationExpirationDays());
    }

    @Override
    @Transactional(readOnly=false)
    public boolean declineInvitation(Long initiativeId, String invitationCode) {
        final String METHOD_NAME = "declineInvitation";
        
        Invitation invitation = initiativeDao.getOpenInvitation(initiativeId, invitationCode, initiativeSettings.getInvitationExpirationDays());
        if (invitation == null) {
            log(METHOD_NAME, initiativeId, userService.getCurrentUser(), false);
            return false;
        }
        else {
            // NOTE: There's no need to lock initiative
            initiativeDao.removeInvitation(initiativeId, invitationCode);

            InitiativeManagement initiative = initiativeDao.getInitiativeForManagement(initiativeId, false);
            List<String> authorEmails = initiativeDao.getAuthorEmails(initiativeId);
            emailService.sendInvitationRejectedInfoToVEVs(initiative, invitation.getEmail(), authorEmails);

            log(METHOD_NAME, initiativeId, userService.getCurrentUser(), true);
            return true;
        }
    }
    
    @Override
    @Transactional(readOnly=false)
    public void confirmCurrentAuthor(Long initiativeId) {
        final String METHOD_NAME = "confirmCurrentAuthor";
        User user = userService.getUserInRole(Role.REGISTERED);
        
        InitiativeManagement initiative = getInitiativeForManagement(initiativeId, true);        
        ManagementSettings managementSettings = initiativeSettings.getManagementSettings(initiative, user);
        
        if (managementSettings.isAllowConfirmCurrentAuthor()) {
            initiativeDao.confirmAuthor(initiativeId, user.getId());

            initiative = getInitiativeForManagement(initiativeId, false); // refresh values
            List<String> authorEmails = initiativeDao.getAuthorEmails(initiativeId);
            emailService.sendAuthorConfirmedInfoToVEVs(initiative, authorEmails);

            log(METHOD_NAME, initiativeId, user, true);
        } else {
            throw new IllegalStateException("Cannot confirm current author");
        }
    }
    
    @Override
    @Transactional(readOnly=false)
    public void deleteCurrentAuthor(Long initiativeId) {
        final String METHOD_NAME = "deleteCurrentAuthor";
        User user = userService.getUserInRole(Role.REGISTERED);
        
        InitiativeManagement initiative = getInitiativeForManagement(initiativeId, true);
        ManagementSettings managementSettings = initiativeSettings.getManagementSettings(initiative, user);
        
        if (managementSettings.isAllowDeleteCurrentAuthor()) {
            initiativeDao.deleteAuthor(initiativeId, user.getId());

            Author removedAuthor = initiative.getCurrentAuthor();
            initiative = initiativeDao.getInitiativeForManagement(initiativeId, false); // refresh values
            List<String> authorEmails = initiativeDao.getAuthorEmails(initiativeId);
            emailService.sendAuthorRemovedInfoToVEVs(initiative, removedAuthor, authorEmails);

            log(METHOD_NAME, initiativeId, user, true);
            // TODO: Process implications (to-be-defined)
        } else {
            throw new IllegalStateException("Cannot delete current author");
        }
    }
    
    @Override
    @Transactional(readOnly=false)
    public boolean acceptInvitation(Long initiativeId, String invitationCode, Author author, Errors errors) {
        final String METHOD_NAME = "acceptInvitation";
        User user = userService.getUserInRole(Role.AUTHENTICATED); 
        //NOTE: user must be authenticated _before_ invitation verification, but persisting user happens _after_

        //        Assert.notNull(form, "form");
        Assert.notNull(initiativeId, "initiativeId");
        Assert.notNull(invitationCode, "invitationCode");
        Assert.notNull(author, "author");

        user = userService.currentAsRegisteredUser(); // Requires authenticated user and optionally registers

        InitiativeManagement initiative = initiativeDao.getInitiativeForManagement(initiativeId, true);
        Assert.notNull(initiative, "initiative");
        
        ManagementSettings managementSettings = initiativeSettings.getManagementSettings(initiative, user);
        
        if (managementSettings.isAllowAcceptInvitation()) {
            assignAuthorAutoFields(author, user);
            
            if (validate(author, user, errors)) {
                Invitation invitation = getInvitation(initiativeId, invitationCode);
                Assert.notNull(invitation, "invitation");
                
                if (getAuthor(initiativeId, user.getId()) != null) {
                    initiativeDao.updateAuthor(initiativeId, user.getId(), author);
                    if (author.isUnconfirmed()) {   // does automatic confirmation, if needed
                        initiativeDao.confirmAuthor(initiativeId, user.getId());
                    }
                } else {
                    initiativeDao.insertAuthor(initiativeId, user.getId(), author);
                }
                
                initiativeDao.removeInvitation(initiativeId, invitationCode);
                
                initiative = getInitiativeForManagement(initiativeId, false); // refresh values
                List<String> authorEmails = initiativeDao.getAuthorEmails(initiativeId);
                emailService.sendInvitationAcceptedInfoToVEVs(initiative, authorEmails);
                
                log(METHOD_NAME, initiativeId, user, true);
                return true;
            } else {
                log(METHOD_NAME, initiativeId, user, false);
                return false;
            }
        } else {
            throw new IllegalStateException("Accept invitation is not allowed at this point");
        }
        
    }

    @Override
    @Transactional(readOnly=false)
    // Even if it's not possible to rollback emails, 
    // it is important to have updatestate and removeinvitations in the same transaction
    public void sendToOM(Long initiativeId) {
        final String METHOD_NAME = "sendToOM";
        User user = userService.getUserInRole(Role.REGISTERED);
        
        InitiativeManagement initiative = getInitiativeForManagement(initiativeId, true);
        ManagementSettings managementSettings = initiativeSettings.getManagementSettings(initiative, user);
        
        // Allowed only for authors 
        if (!managementSettings.isAllowSendToOM()) {
            throw new IllegalStateException("Not allowed for current user or current state");
        }
        
        initiativeDao.updateInitiativeState(initiative.getId(), user.getId(), InitiativeState.REVIEW, null);
        initiativeDao.removeInvitations(initiative.getId());
        initiativeDao.removeUnconfirmedAuthors(initiative.getId());
        emailService.sendNotificationToOM(initiative);
        emailService.sendStatusInfoToVEVs(initiative, EmailMessageType.SENT_TO_OM);

        log(METHOD_NAME, initiativeId, user, true);
    }

    @Override
    @Transactional(readOnly=false)
    public void respondByOm(Long initiativeId, boolean accept, String comment, String acceptanceIdentifier) {
        final String METHOD_NAME = "respondByOm";
        User user = userService.getUserInRole(Role.OM);
        
        InitiativeManagement initiative = getInitiativeForManagement(initiativeId, true);
        ManagementSettings managementSettings = initiativeSettings.getManagementSettings(initiative, user);
        
        // Allowed only for non-author OM officials
        if (!managementSettings.isAllowRespondByOM()) {
            throw new IllegalStateException("Not allowed for current user or current state");
        }
        InitiativeState state = accept ? InitiativeState.ACCEPTED : InitiativeState.PROPOSAL;
        DateTime now = new DateTime();

        if (accept) {
            initiativeDao.updateInitiativeStateAndAcceptanceIdentifier(initiative.getId(), user.getId(), state , comment, acceptanceIdentifier);
        } else {
            initiativeDao.updateInitiativeState(initiative.getId(), user.getId(), state , comment);
        }
        // Update initiative dto
        initiative.assignModifierId(user.getId());
        initiative.assignModified(now);
        initiative.assignState(state);
        initiative.assignStateComment(comment);
        initiative.assignStateDate(now);
        if (accept) {
            initiative.setAcceptanceIdentifier(acceptanceIdentifier);
        }

        //emailService.sendResponseByOmToVEVs(initiative, accept, comment);
        emailService.sendStatusInfoToVEVs(initiative, accept ? EmailMessageType.ACCEPTED_BY_OM : EmailMessageType.REJECTED_BY_OM);

        log(METHOD_NAME, initiativeId, user, true);
    }

    @Override
    @Transactional(readOnly=false)
    public boolean updateVRKResolution(InitiativeManagement initiative, Errors bindingResult) {
        final String METHOD_NAME = "updateVRKResolution";
        User user = userService.getUserInRole(Role.VRK);

        InitiativeManagement persistedInitiative = initiativeDao.getInitiativeForManagement(initiative.getId(), true);
        ManagementSettings managementSettings = initiativeSettings.getManagementSettings(persistedInitiative, user);
        
        if (managementSettings.isAllowRespondByVRK() && validate(initiative, user, bindingResult, VRK.class)) {
            initiativeDao.updateVRKResolution(
                    initiative.getId(), 
                    initiative.getVerifiedSupportCount(),
                    initiative.getVerified(),
                    initiative.getVerificationIdentifier(), user.getId());
            emailService.sendVRKResolutionToVEVs(initiativeDao.getInitiativeForManagement(initiative.getId(), false));

            log(METHOD_NAME, initiative.getId(), user, true);
            return true;
        } else {
            log(METHOD_NAME, initiative.getId(), user, false);
            return false;
        }
    }

    private void log(final String method, final Long initiativeId, final User user, final boolean ok) {
        log(method, initiativeId, user, ok, log);
    }
        
    static void log(final String method, final Long id, final User user, final boolean ok, Logger log) {
        if (log.isInfoEnabled()) {
            final String status = ok ? "OK" : "FAIL";
            final String idArg = id != null ? id.toString() : "_"; 
            if (user == null || user.isAnonymous()) {
                log.info("{}({}) {} by ANONYMOUS", new Object[] { method, idArg, status});
            } else {
                if (user.getId() != null) {
                    log.info("{}({}) {} by {} {} ({})", new Object[] { method, idArg, status, user.getFirstNames(), user.getLastName(), user.getId()});
                } else {
                    log.info("{}({}) {} by {} {}", new Object[] { method, idArg, status, user.getFirstNames(), user.getLastName()});
                }
            }
        }
    }

}
