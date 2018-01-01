package pl.lazyteam.pricebreaker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.context.WebContext;
import pl.lazyteam.pricebreaker.entity.User;
import pl.lazyteam.pricebreaker.entity.UserRole;
import pl.lazyteam.pricebreaker.form.LoginForm;
import pl.lazyteam.pricebreaker.service.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Controller
@RequestMapping("/")
public class LoginController {

    @Autowired
    UserServiceImpl userService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, @RequestParam(value="error", required = false) String error){

        model.addAttribute("loginForm", new LoginForm());
        if(error != null){
            model.addAttribute("loginError", true);
        }
        if (false)
        {
            WebContext context = new org.thymeleaf.context.WebContext(null, null, null);
            context.setVariable("loginForm", new LoginForm());
        }
        return "user/login/login";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth != null){
            new SecurityContextLogoutHandler().logout(request,response,auth);
        }

        return "redirect:/home";
    }

    @RequestMapping(value ={"/","/home"}, method = RequestMethod.GET)
    public String home(Model model){
        model.addAttribute("user", userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        return "user/home";
    }
}
