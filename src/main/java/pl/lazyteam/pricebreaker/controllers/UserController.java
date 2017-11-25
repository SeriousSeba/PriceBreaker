package pl.lazyteam.pricebreaker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.context.WebContext;
import pl.lazyteam.pricebreaker.Entity.User;
import pl.lazyteam.pricebreaker.Entity.UserRole;
import pl.lazyteam.pricebreaker.dao.UserDao;
import pl.lazyteam.pricebreaker.form.RegisterForm;
import pl.lazyteam.pricebreaker.service.UserServiceImpl;
import pl.lazyteam.pricebreaker.validator.RegistrationValidator;

@Controller
public class UserController
{
/*    @Autowired
    RegistrationValidator registrationValidator;*/

    @Autowired
    UserServiceImpl userService;

    @Autowired
    RegistrationValidator registrationValidator;


    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(Model model)
    {
        model.addAttribute("registerForm", new RegisterForm());
        //w wersji intellij'a < 2017.3 coś nie działa i bez tego warunku nie wykrywa nazwy obiektu w htmlu
        if (false)
        {
            WebContext context = new org.thymeleaf.context.WebContext(null, null, null);
            context.setVariable("registerForm", new RegisterForm());
        }
        return "register";
    }

    @RequestMapping(value = "/registerSuccess", method = RequestMethod.POST)
    public String submitSuccess(@ModelAttribute("registerForm") RegisterForm registerForm, Model model, BindingResult result)
    {
        registrationValidator.validate(registerForm, result);
        if(result.hasErrors())
        {
            return "register";
        }
        else
        {
            userService.add(registerForm.getUsername(), registerForm.getPassword(), registerForm.getEmail());
            return "registerSuccess";
        }
    }

}