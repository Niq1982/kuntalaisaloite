package fi.om.municipalityinitiative.newdto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidCreateFranchiseValidator implements ConstraintValidator<ValidCreateFranchise,InitiativeCreateParticipantValidationInfo> {

    @Override
    public void initialize(ValidCreateFranchise constraintAnnotation) {
    }

    @Override
    public boolean isValid(InitiativeCreateParticipantValidationInfo value, ConstraintValidatorContext context) {

        if (value.getMunicipality() == null || value.getHomeMunicipality() == null)
            return true; // This should be validated else where.

        if (Boolean.TRUE.equals(value.getCollectable()) &&
                value.getMunicipality().equals(value.getHomeMunicipality())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("ValidCreateFranchise")
                    .addNode("franchise")
                    .addConstraintViolation();
            return value.getFranchise() != null;
        }
        return true;
    }
}