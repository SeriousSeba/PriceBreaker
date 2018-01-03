package pl.lazyteam.pricebreaker.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import pl.lazyteam.pricebreaker.form.RegisterForm;
import pl.lazyteam.pricebreaker.service.UserServiceImpl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RegistrationValidator implements Validator
{
    @Autowired
    UserServiceImpl userService;

    private Pattern pattern;
    private Matcher matcher;
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$";

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

        if(!emailIsValid(registerForm.getEmail()))
        {
            errors.rejectValue("email", "invalid.email");
        }

        if(userService.emailExists(registerForm.getEmail()))
        {
            errors.rejectValue("email", "exists.email");
        }
    }

    private boolean emailIsValid(String email)
    {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
