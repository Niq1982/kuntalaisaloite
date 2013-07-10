package fi.om.municipalityinitiative.service.ui;

import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.service.PrepareSafeInitiativeCreateDto;
import fi.om.municipalityinitiative.dto.ui.AuthorInvitationUIConfirmDto;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.dto.ui.ParticipantUICreateDto;
import fi.om.municipalityinitiative.dto.ui.PrepareSafeInitiativeUICreateDto;
import fi.om.municipalityinitiative.dto.user.LoginUserHolder;
import fi.om.municipalityinitiative.dto.user.User;
import fi.om.municipalityinitiative.dto.user.VerifiedUser;
import fi.om.municipalityinitiative.exceptions.InvalidHomeMunicipalityException;
import fi.om.municipalityinitiative.service.operations.VerifiedInitiativeServiceOperations;
import fi.om.municipalityinitiative.util.Maybe;

import javax.annotation.Resource;

import java.util.Locale;

public class VerifiedInitiativeService {

    @Resource
    private VerifiedInitiativeServiceOperations operations;

    public long prepareSafeInitiative(LoginUserHolder loginUserHolder, PrepareSafeInitiativeUICreateDto uiCreateDto) {

        VerifiedUser verifiedUser = loginUserHolder.getVerifiedUser();

        if (municipalityMismatch(uiCreateDto.getMunicipality(), uiCreateDto.getUserGivenHomeMunicipality(), verifiedUser.getHomeMunicipality())) {
            throw new InvalidHomeMunicipalityException("Unable to create initiative for municipality with id " + uiCreateDto.getMunicipality());
        }

        // TODO: Check user age.

        Long initiativeId = operations.doPrepareSafeInitiative(verifiedUser, uiCreateDto);

        // TODO: Email

        return initiativeId;

    }

    public void confirmVerifiedAuthorInvitation(LoginUserHolder loginUserHolder, Long initiativeId, AuthorInvitationUIConfirmDto confirmDto, Locale locale) {
        VerifiedUser verifiedUser = loginUserHolder.getVerifiedUser();
        if (municipalityMismatch(confirmDto.getMunicipality(), confirmDto.getHomeMunicipality(), verifiedUser.getHomeMunicipality())) {
            throw new InvalidHomeMunicipalityException("Unable to create initiative for municipality with id " + confirmDto.getMunicipality());
        }

        operations.doConfirmInvitation(verifiedUser, initiativeId, confirmDto);

        // TODO: Already participated/author
    }

    public void createParticipant(LoginUserHolder initiativeId, Long participant, ParticipantUICreateDto loginUserHolder) {
        //To change body of created methods use File | Settings | File Templates.
        throw new RuntimeException("Not implemented");
    }

    private static boolean municipalityMismatch(Long initiativeMunicipality, Long userGivenHomeMunicipality, Maybe<Municipality> vetumaMunicipality) {
        return vetumaMunicipalityReceivedAndMismatches(vetumaMunicipality, initiativeMunicipality)
                || vetumaMunicipalityNotReceivedAndUserGivenMismatches(vetumaMunicipality, initiativeMunicipality, userGivenHomeMunicipality);
    }


    private static boolean vetumaMunicipalityNotReceivedAndUserGivenMismatches(Maybe<Municipality> vetumaMunicipality, Long initiativeMunicipality, Long userGivenHomeMunicipality) {
        return vetumaMunicipality.isNotPresent()
                && !initiativeMunicipality.equals(userGivenHomeMunicipality);
    }

    private static boolean vetumaMunicipalityReceivedAndMismatches(Maybe<Municipality> vetumaMunicipality, Long initiativeMunicipality) {
        return vetumaMunicipality.isPresent()
                && !initiativeMunicipality.equals(vetumaMunicipality.get().getId());
    }
}
