package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.exceptions.OperationNotAllowedException;
import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.service.Municipality;
import fi.om.municipalityinitiative.newdto.ui.ContactInfo;
import fi.om.municipalityinitiative.newdto.ui.InitiativeDraftUIEditDto;
import fi.om.municipalityinitiative.util.Locales;
import fi.om.municipalityinitiative.util.ReflectionTestUtils;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class InitiativeManagementServiceIntegrationTest extends ServiceIntegrationTestBase {

    @Resource
    InitiativeManagementService service;

    @Resource
    TestHelper testHelper;

    private static Municipality testMunicipality;

    private static Municipality participantMunicipality;

    @Before
    public void setup() {
        testHelper.dbCleanup();

        String municipalityName = "Test municipality";
        testMunicipality = new Municipality(testHelper.createTestMunicipality(municipalityName), municipalityName, municipalityName, false);

        municipalityName = "Participant municipality";
        participantMunicipality = new Municipality(testHelper.createTestMunicipality(municipalityName), municipalityName, municipalityName, false);

    }

    @Test(expected = OperationNotAllowedException.class)
    public void get_initiative_for_edit_fails_if_initiative_accepted() {
        Long collectableAccepted = testHelper.createCollectableAccepted(testMunicipality.getId());
        service.getInitiativeDraftForEdit(collectableAccepted, TestHelper.authorLoginUserHolder);
    }

    @Test(expected = AccessDeniedException.class)
    public void editing_initiative_throws_exception_if_wrong_author() {
        Long initiativeId = testHelper.createDraft(testMunicipality.getId());

        InitiativeDraftUIEditDto editDto = InitiativeDraftUIEditDto.parse(ReflectionTestUtils.modifyAllFields(new Initiative()), new ContactInfo());

        service.editInitiativeDraft(initiativeId, TestHelper.unknownLoginUserHolder, editDto);
    }

    @Test(expected = AccessDeniedException.class)
    public void getting_initiativeDraft_for_edit_throws_exception_if_not_allowed() {
        service.getInitiativeDraftForEdit(null, TestHelper.unknownLoginUserHolder);
    }

    @Test(expected = OperationNotAllowedException.class)
    public void edit_initiative_fails_if_initiative_accepted() {
        Long collectableAccepted = testHelper.createCollectableAccepted(testMunicipality.getId());
        service.editInitiativeDraft(collectableAccepted, TestHelper.authorLoginUserHolder, new InitiativeDraftUIEditDto());
    }

    @Test
    public void editing_initiative_updates_all_required_fields() {

        Long initiativeId = testHelper.createDraft(testMunicipality.getId());

        InitiativeDraftUIEditDto editDto = InitiativeDraftUIEditDto.parse(
                ReflectionTestUtils.modifyAllFields(new Initiative()),
                ReflectionTestUtils.modifyAllFields(new ContactInfo())
        );

        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setEmail("updated email");
        contactInfo.setAddress("updated address");
        contactInfo.setPhone("updated phone");
        contactInfo.setName("updated author name");
        contactInfo.setShowName(false); // As far as default is true ...
        editDto.setContactInfo(contactInfo);
        editDto.setName("updated initiative name");
        editDto.setProposal("updated proposal");
        editDto.setExtraInfo("updated extrainfo");

        service.editInitiativeDraft(initiativeId, TestHelper.authorLoginUserHolder, editDto);

        InitiativeDraftUIEditDto updated = service.getInitiativeDraftForEdit(initiativeId, TestHelper.authorLoginUserHolder);

        ReflectionTestUtils.assertReflectionEquals(updated.getContactInfo(), contactInfo);
        assertThat(updated.getName(), is(editDto.getName()));
        assertThat(updated.getProposal(), is(editDto.getProposal()));
        assertThat(updated.getContactInfo().isShowName(), is(editDto.getContactInfo().isShowName()));
        assertThat(updated.getExtraInfo(), is(editDto.getExtraInfo()));
        ReflectionTestUtils.assertNoNullFields(updated);

    }
}
