package fi.om.municipalityinitiative.service;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;

import javax.annotation.Resource;

public class ValidationServiceImpl implements ValidationService{

    @Resource
    SmartValidator validator;

    @Override
    public boolean validationSuccessful(Object object, BindingResult bindingResult, Model model) {
        validator.validate(object, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult);
            return false;
        }
        return true;
    }

    @Override
    public boolean validationErrors(Object o, BindingResult bindingResult, Model model) {
        return !validationSuccessful(o, bindingResult, model);
    }
}
