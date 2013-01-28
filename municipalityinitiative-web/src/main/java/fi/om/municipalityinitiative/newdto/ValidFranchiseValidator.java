package fi.om.municipalityinitiative.newdto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidFranchiseValidator implements ConstraintValidator<ValidFranchise, ParticipantFranchise> {

    @Override
    public void initialize(ValidFranchise constraintAnnotation) {
    }

    @Override
    public boolean isValid(ParticipantFranchise value, ConstraintValidatorContext context) {

        if (value.getMunicipality() == null || value.getHomeMunicipality() == null)
            return true; // This should be validated else where.

        if (value.getMunicipality().equals(value.getHomeMunicipality())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("ValidFranchise")
                    .addNode("franchise")
                    .addConstraintViolation();
            return value.getFranchise() != null;
        }
        return true;
    }
}
