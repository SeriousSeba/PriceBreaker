package pl.lazyteam.pricebreaker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.context.WebContext;
import pl.lazyteam.pricebreaker.entity.User;
import pl.lazyteam.pricebreaker.event.OnRegistrationCompleteEvent;
import pl.lazyteam.pricebreaker.form.PasswordChangeForm;
import pl.lazyteam.pricebreaker.form.RegisterForm;
import pl.lazyteam.pricebreaker.service.UserServiceImpl;
import pl.lazyteam.pricebreaker.validator.PasswordChangeValidator;
import pl.lazyteam.pricebreaker.validator.RegistrationValidator;

import java.util.ArrayList;
import java.util.Locale;

@Controller
public class UserController
{

    @Autowired
    UserServiceImpl userService;

    @Autowired
    RegistrationValidator registrationValidator;

    @Autowired
    PasswordChangeValidator passwordChangeValidator;

    @Autowired
    ApplicationEventPublisher eventPublisher;




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
        model.addAttribute("user", userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        return "user/password/changePassword";
    }

    @PostMapping(value = "passwordChangeSuccess")
    public String passwordChangeSuccess(@ModelAttribute("passwordChangeForm") PasswordChangeForm passwordChangeForm, Model model, BindingResult result)
    {
        passwordChangeValidator.validate(passwordChangeForm, result);
        if(result.hasErrors())
        {
            model.addAttribute("user", userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
            return "user/password/changePassword";
        }
        else
        {
            userService.update(passwordChangeForm.getUsername(), passwordChangeForm.getNewPassword());
            model.addAttribute("user", userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
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
    public String submitSuccess(@ModelAttribute("registerForm") RegisterForm registerForm, Model model, BindingResult result, WebRequest request)
    {
        registrationValidator.validate(registerForm, result);
        if(result.hasErrors())
        {
            return "user/register/register";
        }
        else
        {
            userService.add(registerForm.getUsername(), registerForm.getPassword(), registerForm.getEmail());
            try
            {
                User user = userService.findUserByUsername(registerForm.getUsername());
                String appUrl = request.getContextPath();
                eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, appUrl));
            }catch (Exception e)
            {
                System.out.println("Email error");
                return "user/register/emailError";
            }

            return "user/register/registerSuccess";
        }
    }

    @GetMapping(value = "/registerConfirm")
    public String registerConfirm(Model model, @RequestParam("token") String token,  RedirectAttributes redirectAttributes)
    {
        String result = userService.validateVerificationToken(token);
        if(result.equals("valid"))
        {
            redirectAttributes.addFlashAttribute("message", "Account activated");
            return "redirect:/accountActivated";
        }
        redirectAttributes.addFlashAttribute("message", "Account activation link has expired or is invalid");
        return "redirect:/emailError";
    }

    @GetMapping(value = "/accessDenied")
    public String accessDenied()
    {
        return "user/error/accessDenied";
    }

    @GetMapping(value = "/emailError")
    public String emailError()
    {
        return "user/error/emailError";
    }

    @GetMapping(value = "/accountActivated")
    public String accountActivated()
    {
        return "user/register/accountActivated";
    }

}