package fi.om.municipalityinitiative.validation;

import org.joda.time.DateTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotTooFastSubmitValidator implements ConstraintValidator<NotTooFastSubmit, Long> {

    public static final int MIN_SUBMIT_TIME_MILLIS = 10000;

    private static boolean validationEnabled = true;

    @Override
    public void initialize(NotTooFastSubmit constraintAnnotation) {
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {

        long now = DateTime.now().getMillis();

        return (!validationEnabled
                || (value != null && (now - value) > MIN_SUBMIT_TIME_MILLIS));
    }

    public static void disable() {
        validationEnabled = false;
    }
}
