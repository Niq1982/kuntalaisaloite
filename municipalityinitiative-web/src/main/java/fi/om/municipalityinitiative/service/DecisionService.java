package fi.om.municipalityinitiative.service;


import fi.om.municipalityinitiative.dao.DecisionAttachmentDao;
import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dto.service.DecisionAttachmentFile;
import fi.om.municipalityinitiative.dto.ui.MunicipalityDecisionDto;
import fi.om.municipalityinitiative.service.ui.MunicipalityDecisionInfo;
import fi.om.municipalityinitiative.util.Maybe;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;


public class DecisionService {

    @Resource
    private DecisionAttachmentDao decisionAttachmentDao;

    @Resource
    private InitiativeDao initiativeDao;

    public Maybe<MunicipalityDecisionInfo> getDecision(Long initiativeId) {
        return Maybe.absent();
    }

    @Transactional
    public void setDecision(MunicipalityDecisionDto decision, Long initiativeId) {
        decisionAttachmentDao.removeAttachments(initiativeId);
        addAttachments(decision.getFiles(), initiativeId);


        // TODO actually save attachments to disk
    }


    private void addAttachments(List<MultipartFile> attachments, Long initiativeId) {
        for (MultipartFile attachment: attachments) {
            decisionAttachmentDao.addAttachment(new DecisionAttachmentFile(attachment.getOriginalFilename(), attachment.getContentType(), resolveFileType(attachment), initiativeId), initiativeId);
        }
    }

    private String resolveFileType(MultipartFile attachment) {
        return null;
    }
}
