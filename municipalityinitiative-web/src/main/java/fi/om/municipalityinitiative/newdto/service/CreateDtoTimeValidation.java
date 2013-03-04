package fi.om.municipalityinitiative.newdto.service;

import fi.om.municipalityinitiative.validation.NotTooFastSubmit;
import org.joda.time.DateTime;

public class CreateDtoTimeValidation {

    @NotTooFastSubmit
    private Long randomNumber = DateTime.now().getMillis();

    public Long getRandomNumber() {
        return randomNumber;
    }

    public void setRandomNumber(Long randomNumber) {
        this.randomNumber = randomNumber;
    }
}
