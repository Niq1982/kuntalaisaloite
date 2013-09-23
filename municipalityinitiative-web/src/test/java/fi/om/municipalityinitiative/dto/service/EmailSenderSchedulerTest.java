package fi.om.municipalityinitiative.dto.service;

import javax.annotation.Resource;

public class EmailSenderSchedulerTest extends EmailSenderTestBase {

    @Resource
    private EmailSenderScheduler emailSenderScheduler;

    @Override
    protected void doSendEmailsTask() throws InterruptedException {
        emailSenderScheduler.sendEmails();
    }
}
