package fi.om.municipalityinitiative.dto.service;

public class EmailSenderTest extends EmailSenderTestBase {

    @Override
    protected void doSendEmailsTask() throws InterruptedException {
        emailSender.sendNextEmail();
    }
}
