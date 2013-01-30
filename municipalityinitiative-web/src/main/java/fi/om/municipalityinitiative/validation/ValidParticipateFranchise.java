package fi.om.municipalityinitiative.validation;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target( { TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = ValidParticipateFranchiseValidator.class)
@Documented
public @interface ValidParticipateFranchise {

    String message() default "{NotNull}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

