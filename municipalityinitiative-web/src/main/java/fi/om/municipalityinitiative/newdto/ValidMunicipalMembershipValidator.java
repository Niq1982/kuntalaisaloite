package fi.om.municipalityinitiative.newdto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidMunicipalMembershipValidator  implements ConstraintValidator<ValidMunicipalMembership, ParticipantFranchise> {

    @Override
    public void initialize(ValidMunicipalMembership constraintAnnotation) {
    }

    @Override
    public boolean isValid(ParticipantFranchise value, ConstraintValidatorContext context) {

        if (value.getMunicipality() == null || value.getHomeMunicipality() == null)
            return true; // This should be validated else where.

        if (!value.getMunicipality().equals(value.getHomeMunicipality())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("ValidMunicipalMembership")
                    .addNode("municipalMembership")
                    .addConstraintViolation();
            return Boolean.TRUE.equals(value.getMunicipalMembership());
        }
        return true;
    }
}

