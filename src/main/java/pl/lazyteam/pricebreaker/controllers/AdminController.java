package pl.lazyteam.pricebreaker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.lazyteam.pricebreaker.service.UserServiceImpl;

@Controller
public class AdminController
{

    @Autowired
    UserServiceImpl userService;


    @GetMapping(value ="/users")
    public String showUsers(Model model, RedirectAttributes redirectAttributes)
    {
        model.addAttribute("users", userService.list());
        return "admin/users/users";
    }

    @GetMapping(value ="/users/edit/{username}")
    public String edit(@PathVariable("username") String username, Model model)
    {
        model.addAttribute("user", userService.findUserByUsername(username));
        return "admin/users/edit";
    }

    @GetMapping(value ="/users/edit/deleteUser/{username}")
    public String deleteUser(@PathVariable("username") String username, Model model, RedirectAttributes redirectAttributes)
    {
        userService.delete(username);
        redirectAttributes.addFlashAttribute("message", username + " has been successfully deleted");
        return "redirect:/users";
    }


}