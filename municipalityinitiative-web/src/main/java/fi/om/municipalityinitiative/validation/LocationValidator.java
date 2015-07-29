package fi.om.municipalityinitiative.validation;


import com.google.common.base.Strings;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LocationValidator implements ConstraintValidator<ValidLocation, InitiativeWithLocationInformation>{

    @Override
    public void initialize(ValidLocation constraintAnnotation) {
    }

    @Override
    public boolean isValid(InitiativeWithLocationInformation value, ConstraintValidatorContext context) {
        if (value.getLocationLat() == null && value.getLocationLng() == null && Strings.isNullOrEmpty(value.getLocationDescription())) {
            return true;
        }
        else if (value.getLocationLat() != null && value.getLocationLng() != null && !Strings.isNullOrEmpty(value.getLocationDescription())) {
            return true;
        }
        else {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("ValidLocation")
                    .addNode("locationDescription")
                    .addConstraintViolation();
            return false;
        }

    }
}
