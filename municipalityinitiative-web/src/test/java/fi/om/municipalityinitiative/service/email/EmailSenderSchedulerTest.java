package fi.om.municipalityinitiative.service.email;

import javax.annotation.Resource;

public class EmailSenderSchedulerTest extends EmailSenderTestBase {

    @Resource
    private EmailSenderScheduler emailSenderScheduler;

    @Override
    protected void doSendEmailsTask() throws InterruptedException {
        emailSenderScheduler.sendEmails();
    }
}
