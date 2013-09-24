package fi.om.municipalityinitiative.service.email;

public class EmailSenderTest extends EmailSenderTestBase {

    @Override
    protected void doSendEmailsTask() throws InterruptedException {
        emailSender.sendNextEmail();
    }
}
