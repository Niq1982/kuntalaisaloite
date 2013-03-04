package fi.om.municipalityinitiative.validation;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target( { FIELD, TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = NotTooFastSubmitValidator.class)
@Documented
public @interface NotTooFastSubmit {

    String message() default "{NotTooFastSubmit}";

    Class<? extends Payload>[] payload() default {};

    Class<?>[] groups() default {};

}
