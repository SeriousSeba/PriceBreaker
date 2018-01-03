package pl.lazyteam.pricebreaker.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import pl.lazyteam.pricebreaker.form.PasswordChangeForm;
import pl.lazyteam.pricebreaker.service.UserServiceImpl;

@Component
public class PasswordChangeValidator implements Validator
{

    @Autowired
    UserServiceImpl userService;

    public boolean supports(Class<?> aClass)
    {
        return PasswordChangeForm.class.isAssignableFrom(aClass);
    }

    public void validate(Object o, Errors errors)
    {
        PasswordChangeForm passwordChangeForm = (PasswordChangeForm) o;

        ValidationUtils.rejectIfEmpty(errors, "newPassword", "notEmpty.password");
        ValidationUtils.rejectIfEmpty(errors, "confirmNewPassword", "notEmpty.confirmPassword");

        if (!userService.validatePassword(passwordChangeForm.getOldPassword()))
        {
            errors.rejectValue("oldPassword", "notMatch.oldPassword");
        }

        if(passwordChangeForm.getNewPassword() != null && passwordChangeForm.getConfirmNewPassword() != null && !passwordChangeForm.getNewPassword().equals(passwordChangeForm.getConfirmNewPassword()))
        {
            errors.rejectValue("newPassword", "notMatch.confirmPassword");
        }
    }
}
