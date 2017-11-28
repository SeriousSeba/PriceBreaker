package pl.lazyteam.pricebreaker.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import pl.lazyteam.pricebreaker.form.RegisterForm;
import pl.lazyteam.pricebreaker.service.UserServiceImpl;

@Component
public class RegistrationValidator implements Validator
{
    @Autowired
    UserServiceImpl userService;

    public boolean supports(Class<?> aClass)
    {
        return RegisterForm.class.isAssignableFrom(aClass);
    }

    public void validate(Object o, Errors errors)
    {
        RegisterForm registerForm = (RegisterForm) o;

        ValidationUtils.rejectIfEmpty(errors, "username", "notEmpty.username");
        ValidationUtils.rejectIfEmpty(errors, "password", "notEmpty.password");
        ValidationUtils.rejectIfEmpty(errors, "confirmPassword", "notEmpty.confirmPassword");
        ValidationUtils.rejectIfEmpty(errors, "email", "notEmpty.email");


        if(registerForm.getPassword() != null && registerForm.getConfirmPassword() != null && !registerForm.getPassword().equals(registerForm.getConfirmPassword()))
        {
            errors.rejectValue("password", "notMatch.confirmPassword");
        }

        if(userService.userExists(registerForm.getUsername()))
        {
            errors.rejectValue("username", "exists.username");
        }

        if(userService.emailExists(registerForm.getEmail()))
        {
            errors.rejectValue("email", "exists.email");
        }
    }
}
