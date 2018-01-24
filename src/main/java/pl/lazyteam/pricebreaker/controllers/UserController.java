package pl.lazyteam.pricebreaker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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
import pl.lazyteam.pricebreaker.dao.DefaultFlagsDao;
import pl.lazyteam.pricebreaker.dao.ProductDAO;
import pl.lazyteam.pricebreaker.dao.ProductFlagsDao;
import pl.lazyteam.pricebreaker.entity.DefaultUserFlags;
import pl.lazyteam.pricebreaker.entity.ProductFlags;
import pl.lazyteam.pricebreaker.entity.ProductInfo;
import pl.lazyteam.pricebreaker.entity.User;
import pl.lazyteam.pricebreaker.event.OnRegistrationCompleteEvent;
import pl.lazyteam.pricebreaker.form.FlagEditForm;
import pl.lazyteam.pricebreaker.form.PasswordChangeForm;
import pl.lazyteam.pricebreaker.form.RegisterForm;
import pl.lazyteam.pricebreaker.service.UserServiceImpl;
import pl.lazyteam.pricebreaker.validator.PasswordChangeValidator;
import pl.lazyteam.pricebreaker.validator.RegistrationValidator;

import java.util.*;

/**
 * UserController class allows user to manage account and product on his list.
 */
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

    @Autowired
    ProductDAO productDAO;

    @Autowired
    ProductFlagsDao productFlagsDao;

    @Autowired
    DefaultFlagsDao defaultFlagsDao;


    /**
     * Method allows user to change his password
     * @param username name of the user
     * @param model holder for model attributes
     * @return
     */
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

    /**
     * Method redirecting user to change his password
     * @param model holder for model attributes
     * @return redirect path to place where user can change password
     */
    @GetMapping(value ="/changePassword")
    public String changePassword(Model model)
    {
        User user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return "redirect:/changePassword/" + user.getUsername();
    }

    /**
     * Method shows user a page when password is successfully changed.
     * @param passwordChangeForm form with information about new password
     * @param model holder for model attributes
     * @param result redirect path
     * @return
     */
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

    /**
     * Method that allows user to register. It adds a register form to page.
     * @param model holder for model attributes
     * @return redirect path
     */
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

    /**
     * Method that validates register form and adds user.
     * @param registerForm form with user data
     * @param result result of validation
     * @param request request
     * @return redirect path
     */
    @PostMapping(value = "/registerSuccess")
    public String submitSuccess(@ModelAttribute("registerForm") RegisterForm registerForm, BindingResult result, WebRequest request)
    {
        registrationValidator.validate(registerForm, result);
        if(result.hasErrors())
        {
            return "user/register/register";
        }
        else
        {
            userService.add(registerForm.getUsername(), registerForm.getPassword(), registerForm.getEmail());
            defaultFlagsDao.save(new DefaultUserFlags(userService.findUserByUsername(registerForm.getUsername())));
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

    /**
     * Method that activate account.
     * @param model holder for model attributes
     * @param token user token
     * @param redirectAttributes redirect attributes
     * @return redirect path
     */
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

    /**
     * Method that allows user to get list of his products.
     * @param model holder for model attributes
     * @return
     */
    @GetMapping("/user/products")
    public Set<ProductInfo> getAllProducts(Model model){
        User user=userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("user", user);
        Set<ProductInfo> productsSet = user.getProducts();
        model.addAttribute("productsSet", productsSet);
        return user.getProducts();
    }

    /**
     * Method that allows to edit user product
     * @param id product id
     * @param model holder for model attributes
     * @return
     */
    @GetMapping(value = "/editProduct/{id}")
    public String editProduct(@PathVariable("id") Long id, Model model)
    {
        ProductInfo product = productDAO.getOne(id);
        ProductFlags productFlags = productFlagsDao.getOne(id);
        model.addAttribute("product", product);
        model.addAttribute("productFlags", productFlags);
        model.addAttribute("flagEditForm", new FlagEditForm());

        return "user/editProduct";
    }

    /**
     * Method that manage flags in product edit page.
     * @param id product id
     * @param flagEditForm form with informations about flags
     * @return
     */
    @PostMapping(value = "/editProduct/{id}")
    public String editProductButtonPressed(@PathVariable("id") Long id, @ModelAttribute("flagEditForm") FlagEditForm flagEditForm)
    {
        ProductInfo product = productDAO.getOne(id);
        ProductFlags productFlags = new ProductFlags(product,flagEditForm.getPriceChange()/100,!productFlagsDao.getOne(id).isPrice_lowers());
        productFlagsDao.save(productFlags);
        return "redirect:/editProduct/" + id;
    }

    /**
     * Method that updates flag of products.
     * @param id product id
     * @param flagEditForm form with informations about product
     * @return redirect path to list of products
     */
    @PostMapping(value = "/editProduct/confirm/{id}")
    public String editProductConfirmPressed(@PathVariable("id") Long id, @ModelAttribute("flagEditForm") FlagEditForm flagEditForm)
    {
        ProductInfo product = productDAO.getOne(id);
        ProductFlags productFlags = new ProductFlags(product,flagEditForm.getPriceChange()/100,productFlagsDao.getOne(id).isPrice_lowers());
        productFlagsDao.save(productFlags);
        return "redirect:/user/products";
    }

    /**
     * Method that sets default flag for users.
     * @param model holder for model attributes
     * @return
     */
    @GetMapping(value = "/editDefaultFlags")
    public String editDefaultFlags(Model model)
    {
        User user=userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        DefaultUserFlags defaultUserFlags = defaultFlagsDao.getOne(user.getUser_id());
        model.addAttribute("productFlags", defaultUserFlags);
        model.addAttribute("flagEditForm", new FlagEditForm());

        return "user/editDefaultFlags";
    }

    /**
     * Method that manage change of default flags.
     * @param flagEditForm form with information about flags
     * @return redirect path
     */
    @PostMapping(value = "/editDefaultFlags")
    public String editDefaultFlagsButtonPressed(@ModelAttribute("flagEditForm") FlagEditForm flagEditForm)
    {
        User user=userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        DefaultUserFlags defaultUserFlags = defaultFlagsDao.getOne(user.getUser_id());
        defaultUserFlags.setPriceChange(flagEditForm.getPriceChange()/100);
        defaultUserFlags.setPrice_lowers(!defaultUserFlags.isPrice_lowers());
        defaultFlagsDao.save(defaultUserFlags);
        return "redirect:/editDefaultFlags";
    }

    /**
     * Method that changes default flags
     * @param flagEditForm form with information about flags
     * @return redirect path to homepage
     */
    @PostMapping(value = "/editDefaultFlags/confirm")
    public String editDefaultFlagsConfirmPressed(@ModelAttribute("flagEditForm") FlagEditForm flagEditForm)
    {
        User user=userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        DefaultUserFlags defaultUserFlags = defaultFlagsDao.getOne(user.getUser_id());
        defaultUserFlags.setPriceChange(flagEditForm.getPriceChange()/100);
        defaultFlagsDao.save(defaultUserFlags);
        return "redirect:/home";
    }

    /**
     * Method that allows user to add new products.
     * @param productInfo information about product
     * @param model holder for model attributes
     * @return redirect path to list of products
     */
    @PostMapping("/user/products/add")
    public String addProduct(@ModelAttribute(value = "productinfo") ProductInfo productInfo, Model model){
        productInfo.setLastUpdate(new Date());
        User user=userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        productDAO.save(productInfo);

        //default flags
        DefaultUserFlags defaultFlags = defaultFlagsDao.getOne(user.getUser_id());
        ProductFlags productFlag = new ProductFlags(productInfo, defaultFlags.getPriceChange(), defaultFlags.isPrice_lowers());
        productFlagsDao.save(productFlag);

        productInfo=productDAO.getOne(productInfo.getId());
        user.getProducts().add(productInfo);
        productInfo.getUsers().add(user);
        userService.updateUser(user);
        model.addAttribute("user", user);
        model.addAttribute("productsSet", user.getProducts());

        System.out.println(productInfo.getProductUrl());

        return "user/products";
    }


    /**
     * Method that inform user about lack of access.
     * @return redirect path
     */
    @GetMapping(value = "/accessDenied")
    public String accessDenied()
    {
        return "user/error/accessDenied";
    }

    /**
     * Method that inform user about wrong email
     * @return redirect path
     */
    @GetMapping(value = "/emailError")
    public String emailError()
    {
        return "user/error/emailError";
    }

    /**
     * Method that inform user about account activation.
     * @return redirect path
     */
    @GetMapping(value = "/accountActivated")
    public String accountActivated()
    {
        return "user/register/accountActivated";
    }

}