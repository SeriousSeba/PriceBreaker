package pl.lazyteam.pricebreaker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.lazyteam.pricebreaker.service.UserServiceImpl;

/**
 * AdminController is a class that allows the administrator to manage users.
 */
@Controller
public class AdminController
{

    @Autowired
    UserServiceImpl userService;

    /**
     * Method allows the Administrator to list all users in service
     * @param model holder for model attributes
     * @return
     */
    @GetMapping(value ="/users")
    public String showUsers(Model model, RedirectAttributes redirectAttributes)
    {
        model.addAttribute("user", userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));

        model.addAttribute("users", userService.list());
        return "admin/users/users";
    }

    /**
     * Method allows the Administrator to delete user which name is passed as parameter
     * @param username name of the user
     * @param model holder for model attributes
     * @return
     */
    @GetMapping(value ="/users/deleteUser/{username}")
    public String deleteUser(@PathVariable("username") String username, Model model, RedirectAttributes redirectAttributes)
    {
        userService.delete(username);
        redirectAttributes.addFlashAttribute("message", username + " has been successfully deleted");
        return "redirect:/users";
    }


}