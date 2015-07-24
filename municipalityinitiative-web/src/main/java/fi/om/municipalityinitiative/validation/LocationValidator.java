package fi.om.municipalityinitiative.validation;


import com.google.common.base.Strings;
import fi.om.municipalityinitiative.dto.ui.InitiativeDraftUIEditDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LocationValidator implements ConstraintValidator<ValidLocation, InitiativeDraftUIEditDto>{

    @Override
    public void initialize(ValidLocation constraintAnnotation) {
    }

    @Override
    public boolean isValid(InitiativeDraftUIEditDto value, ConstraintValidatorContext context) {
        if (Strings.isNullOrEmpty(value.getLocationLat()) && Strings.isNullOrEmpty(value.getLocationLng()) && Strings.isNullOrEmpty(value.getLocationDescription())) {
            return true;
        }
        else if (!Strings.isNullOrEmpty(value.getLocationLat()) && !Strings.isNullOrEmpty(value.getLocationLng()) && !Strings.isNullOrEmpty(value.getLocationDescription())) {
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
