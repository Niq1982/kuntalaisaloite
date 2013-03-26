package fi.om.municipalityinitiative.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidMunicipalMembershipValidator  implements ConstraintValidator<ValidMunicipalMembership, ValidMunicipalMembershipInfo> {

    @Override
    public void initialize(ValidMunicipalMembership constraintAnnotation) {
    }

    @Override
    public boolean isValid(ValidMunicipalMembershipInfo value, ConstraintValidatorContext context) {

        if (value.getMunicipality() == null || value.getHomeMunicipality() == null)
            return true; // This should be validated else where.

        if (municipalitiesDiffer(value)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("ValidMunicipalMembership")
                    .addNode("municipalMembership")
                    .addConstraintViolation();
            return value.hasMunicipalMembership();
        }
        return true;
    }

    private static boolean municipalitiesDiffer(ValidMunicipalMembershipInfo value) {
        return !value.getMunicipality().equals(value.getHomeMunicipality());
    }
}

