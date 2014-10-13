package fi.om.municipalityinitiative.service.email;

import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.util.InitiativeState;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

public class EmailReportService {

    @Resource
    private EmailService emailService;

    @Resource
    private InitiativeDao initiativeDao;

    @Transactional(readOnly = false)
    public void sendReportEmailsForInitiativesAcceptedButNotPublished() {
        for (Initiative initiative : getInitiativesAcceptedButNotPublishedIn(Days.days(14))) {
            emailService.sendInitiativeAcceptedButNotPublishedEmail(initiative);
            initiativeDao.markInitiativeReportSent(initiative.getId(), EmailReportType.IN_ACCEPTED, new DateTime());
        }
    }

    private List<Initiative> getInitiativesAcceptedButNotPublishedIn(Days days) {
        List<Initiative> result = Lists.newArrayList();

        for (Initiative initiative : initiativeDao.findAllByStateChangeAfter(InitiativeState.ACCEPTED, new LocalDate().minus(days))) {
            if (initiative.getLastEmailReportType() == null) {
                result.add(initiative);
            }
        }
        return result;

    }

}
