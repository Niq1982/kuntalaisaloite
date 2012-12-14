package fi.om.municipalityinitiative.validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import fi.om.municipalityinitiative.dto.AuthorRole;

@Target( { METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = AuthorRoleValidator.class)
@Documented
public @interface ValidAuthorRole {

    String message() default "{ValidAuthorRole}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    AuthorRole role();
    
}
