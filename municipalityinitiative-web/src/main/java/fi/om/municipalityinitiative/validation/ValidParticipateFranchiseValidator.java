package fi.om.municipalityinitiative.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidParticipateFranchiseValidator implements ConstraintValidator<ValidParticipateFranchise, ParticipantValidationInfo> {

    @Override
    public void initialize(ValidParticipateFranchise constraintAnnotation) {
    }

    @Override
    public boolean isValid(ParticipantValidationInfo value, ConstraintValidatorContext context) {

        if (value.getMunicipality() == null || value.getHomeMunicipality() == null)
            return true; // This should be validated else where.

        if (value.getMunicipality().equals(value.getHomeMunicipality())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("ValidParticipateFranchise")
                    .addNode("franchise")
                    .addConstraintViolation();
            return value.getFranchise() != null;
        }
        return true;
    }
}
