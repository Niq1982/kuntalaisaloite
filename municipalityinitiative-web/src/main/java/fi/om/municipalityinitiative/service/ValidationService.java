package fi.om.municipalityinitiative.service;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

public interface ValidationService {
    boolean validationSuccessful(Object object, BindingResult bindingResult, Model model);
    boolean validatorErrors(Object o, BindingResult bindingResult, Model model);

}
