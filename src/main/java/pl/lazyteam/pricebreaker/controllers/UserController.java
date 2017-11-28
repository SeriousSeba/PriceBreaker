package pl.lazyteam.pricebreaker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.thymeleaf.context.WebContext;
import pl.lazyteam.pricebreaker.form.PasswordChangeForm;
import pl.lazyteam.pricebreaker.form.RegisterForm;
import pl.lazyteam.pricebreaker.service.UserServiceImpl;
import pl.lazyteam.pricebreaker.validator.PasswordChangeValidator;
import pl.lazyteam.pricebreaker.validator.RegistrationValidator;

import java.util.ArrayList;

@Controller
public class UserController
{

    @Autowired
    UserServiceImpl userService;

    @Autowired
    RegistrationValidator registrationValidator;

    @Autowired
    PasswordChangeValidator passwordChangeValidator;



    @GetMapping(value ="/changePassword/{username}")
    public String changePassword(@PathVariable("username") String username, Model model)
    {

        if (false)
        {
            WebContext context = new org.thymeleaf.context.WebContext(null, null, null);
            context.setVariable("passwordChangeForm", new PasswordChangeForm());
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(!auth.getName().equals(username) && !(new ArrayList<GrantedAuthority>(auth.getAuthorities()).get(0).getAuthority()).equals("ROLE_ADMIN"))
        {
            return "user/error/accessDenied";
        }

        PasswordChangeForm passwordChangeForm = new PasswordChangeForm();
        passwordChangeForm.setUsername(username);
        model.addAttribute("passwordChangeForm", passwordChangeForm);
        return "user/password/changePassword";
    }

    @PostMapping(value = "passwordChangeSuccess")
    public String passwordChangeSuccess(@ModelAttribute("passwordChangeForm") PasswordChangeForm passwordChangeForm, Model model, BindingResult result)
    {
        passwordChangeValidator.validate(passwordChangeForm, result);
        if(result.hasErrors())
        {
            return "user/password/changePassword";
        }
        else
        {
            userService.update(passwordChangeForm.getUsername(), passwordChangeForm.getNewPassword());
            return "user/password/passwordChangeSuccess";
        }
    }

    @GetMapping(value = "/register")
    public String register(Model model)
    {
        if (false)
        {
            WebContext context = new org.thymeleaf.context.WebContext(null, null, null);
            context.setVariable("registerForm", new RegisterForm());
        }
        model.addAttribute("registerForm", new RegisterForm());
        return "user/register/register";
    }

    @PostMapping(value = "/registerSuccess")
    public String submitSuccess(@ModelAttribute("registerForm") RegisterForm registerForm, Model model, BindingResult result)
    {
        registrationValidator.validate(registerForm, result);
        if(result.hasErrors())
        {
            return "user/register/register";
        }
        else
        {
            userService.add(registerForm.getUsername(), registerForm.getPassword(), registerForm.getEmail());
            return "user/register/registerSuccess";
        }
    }

    @GetMapping(value = "/accessDenied")
    public String accessDenied()
    {
        return "user/error/accessDenied";
    }

}