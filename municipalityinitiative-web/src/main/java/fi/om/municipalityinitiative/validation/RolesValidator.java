package fi.om.municipalityinitiative.validation;

import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import fi.om.municipalityinitiative.dto.AuthorRole;

public class RolesValidator implements ConstraintValidator<ValidRoles, Set<AuthorRole>> {

    @Override
    public void initialize(ValidRoles constraintAnnotation) {
    }

    @Override
    public boolean isValid(Set<AuthorRole> roles, ConstraintValidatorContext context) {
        if (roles.size() == 0) {
            return false;
        } 
        
        else if (roles.contains(AuthorRole.REPRESENTATIVE) && roles.contains(AuthorRole.RESERVE)) {
            return false;
        }
        else {
            return true;
        }
    }

}
