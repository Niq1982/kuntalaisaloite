package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.dto.service.Initiative;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class YouthInitiativeWebServiceNotifier {

    private final Logger log = LoggerFactory.getLogger(YouthInitiativeWebServiceNotifier.class);

    private String youthInitiativeUrl;

    public YouthInitiativeWebServiceNotifier(String youthInitiativeUrl) {
        this.youthInitiativeUrl = youthInitiativeUrl;
    }

    public void informInitiativePublished(Initiative initiative) {
        if (initiative.getYouthInitiativeId().isPresent()) {
            final String uri = uri(initiative);
            final Status status = Status.of(Status.StatusCode.PUBLISHED);
            backgroundYouthInitiativeCall(uri, status);
        }
        else {
            log.error("Trying to notify nuortenidea.fi for initiative published with id " + initiative.getId() + " but had no youthinitiativeId");
        }
    }

    private String uri(Initiative initiative) {
        return youthInitiativeUrl + "/api/kua/1.0/initiative/" + initiative.getYouthInitiativeId().get().toString() + "/status/create/";
    }

    public void informInitiativeSentToMunicipality(Initiative initiative) {
        if (initiative.getYouthInitiativeId().isPresent()) {
            final String uri = uri(initiative);
            final Status status = Status.of(Status.StatusCode.SENT_TO_MUNICIPALITY);
            backgroundYouthInitiativeCall(uri, status);
        }
        else {
            log.error("Trying to notify nuortenidea.fi for initiative sent to municipality with id " + initiative.getId() + " but had no youthinitiativeId");
        }
    }

    private void backgroundYouthInitiativeCall(final String uri, final Status status) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RestTemplate restTemplate = new RestTemplate();

                log.info("Sending youth-initiative status:" + status);
                log.info("URI:" + uri);

                try {
                    ResponseEntity<Response> responseResponseEntity = restTemplate.postForEntity(uri, status, Response.class);

                    if (responseResponseEntity.getStatusCode() != HttpStatus.OK) {
                        log.error("Got " + responseResponseEntity.getStatusCode());
                        log.error("Got" + responseResponseEntity.getStatusCode() + " - " + responseResponseEntity.getBody());
                    }
                    log.info("Status sent. Got: " + responseResponseEntity.getStatusCode());
                    if (responseResponseEntity.getBody() != null) {
                        log.info("Body: " + responseResponseEntity.getBody());
                    }
                    else {
                        log.info("Body was null.");
                    }
                } catch (Throwable t) {
                    log.error("Unable to mark youthinitiative as " +status.status, t);
                }
            }
        }).start();
    }

    public static class Status {
        public final String status;

        public static Status of(StatusCode code) {
            return new Status(code.statusValue);
        }

        private Status(String status) {
            this.status = status;
        }

        public enum StatusCode {
            PUBLISHED("published"),
            DECISION_GIVEN("decision-given"),
            SENT_TO_MUNICIPALITY("sent-to-municipality");
            public final String statusValue;
            StatusCode(String s) {
                this.statusValue = s;
            }
        }

        @Override
        public String toString() {
            return "Status{" +
                    "status='" + status + '\'' +
                    '}';
        }
    }

    public static class Response {
        public String failure;

        @Override
        public String toString() {
            return "Response{" +
                    "failure='" + failure + '\'' +
                    '}';
        }
    }
}
