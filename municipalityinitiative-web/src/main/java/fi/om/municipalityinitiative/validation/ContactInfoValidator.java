package fi.om.municipalityinitiative.validation;

import static com.google.common.base.Strings.isNullOrEmpty;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import fi.om.municipalityinitiative.dto.Author;
import fi.om.municipalityinitiative.dto.ContactInfo;

public class ContactInfoValidator implements ConstraintValidator<ValidContactInfo, Author> {

    @Override
    public void initialize(ValidContactInfo constraintAnnotation) {
    }

    @Override
    public boolean isValid(Author author, ConstraintValidatorContext context) {
        if (author.isRepresentative() || author.isReserve()) {
            ContactInfo contactInfo = author.getContactInfo();
            if (isNullOrEmpty(contactInfo.getEmail()) 
                    && isNullOrEmpty(contactInfo.getPhone()) 
                    && isNullOrEmpty(contactInfo.getAddress())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("ValidContactInfo")
                .addNode("contactInfo")
                .addConstraintViolation();
                return false;
            }
        }

        return true;
    }

}
