package fi.om.municipalityinitiative.service.ui;

import fi.om.municipalityinitiative.dto.service.PrepareSafeInitiativeCreateDto;
import fi.om.municipalityinitiative.dto.ui.AuthorInvitationUIConfirmDto;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.dto.ui.PrepareSafeInitiativeUICreateDto;
import fi.om.municipalityinitiative.dto.user.LoginUserHolder;
import fi.om.municipalityinitiative.dto.user.User;
import fi.om.municipalityinitiative.dto.user.VerifiedUser;
import fi.om.municipalityinitiative.exceptions.InvalidHomeMunicipalityException;
import fi.om.municipalityinitiative.service.operations.VerifiedInitiativeServiceOperations;

import javax.annotation.Resource;

import java.util.Locale;

public class VerifiedInitiativeService {

    @Resource
    private VerifiedInitiativeServiceOperations operations;

    public long prepareSafeInitiative(LoginUserHolder loginUserHolder, PrepareSafeInitiativeUICreateDto uiCreateDto) {

        VerifiedUser verifiedUser = loginUserHolder.getVerifiedUser();

        if (municipalityMismatch(uiCreateDto, verifiedUser)) {
            throw new InvalidHomeMunicipalityException("Unable to create initiative for municipality with id " + uiCreateDto.getMunicipality());
        }

        // TODO: Check user age.

        PrepareSafeInitiativeCreateDto createDto = new PrepareSafeInitiativeCreateDto();
        createDto.setMunicipality(uiCreateDto.getMunicipality());
        createDto.setHash(verifiedUser.getHash());
        //createDto.setMunicipality(verifiedUser.getMunicipality);
        createDto.setContactInfo(new ContactInfo(verifiedUser.getContactInfo()));
        createDto.setInitiativeType(uiCreateDto.getInitiativeType());
        Long initiativeId = operations.doPrepareSafeInitiative(createDto);

        // TODO: Email

        return initiativeId;

    }

    public void confirmVerifiedAuthorInvitation(LoginUserHolder loginUserHolder, Long initiativeId, AuthorInvitationUIConfirmDto confirmDto, Locale locale) {
        // TODO: Add author and participant
        // TODO: Invalid homeMunicipality
        // TODO: Already participated/author
    }

    private static boolean municipalityMismatch(PrepareSafeInitiativeUICreateDto uiCreateDto, VerifiedUser verifiedUser) {
        return vetumaMunicipalityReceivedAndMismatches(uiCreateDto, verifiedUser)
                || vetumaMunicipalityNotReceivedAndUserGivenMismatches(uiCreateDto, verifiedUser);
    }

    private static boolean vetumaMunicipalityNotReceivedAndUserGivenMismatches(PrepareSafeInitiativeUICreateDto uiCreateDto, VerifiedUser verifiedUser) {
        return verifiedUser.getHomeMunicipality().isNotPresent()
        && !uiCreateDto.getMunicipality().equals(uiCreateDto.getUserGivenHomeMunicipality());
    }

    private static boolean vetumaMunicipalityReceivedAndMismatches(PrepareSafeInitiativeUICreateDto uiCreateDto, VerifiedUser verifiedUser) {
        return verifiedUser.getHomeMunicipality().isPresent()
                && !verifiedUser.getHomeMunicipality().get().getId().equals(uiCreateDto.getMunicipality());
    }
}
