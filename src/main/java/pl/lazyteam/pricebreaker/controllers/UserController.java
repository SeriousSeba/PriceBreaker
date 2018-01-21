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

    @GetMapping("/user/products")
    public Set<ProductInfo> getAllProducts(Model model){
        User user=userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("user", user);
        Set<ProductInfo> productsSet = user.getProducts();
        model.addAttribute("productsSet", productsSet);
        return user.getProducts();
    }

    @GetMapping(value = "/editProduct/{id}")
    public String flagsEditted(@PathVariable("id") Long id, Model model)
    {
        ProductInfo product = productDAO.getOne(id);
        ProductFlags productFlags = productFlagsDao.getOne(id);
        model.addAttribute("product", product);
        model.addAttribute("productFlags", productFlags);
        model.addAttribute("flagEditForm", new FlagEditForm());

        return "user/editProduct";
    }

    //@PostMapping("/user/products/add/{productName}/{productUrl}/{productScore}/{productCategory}/{productBottom}/{productImageUrl}/{productId}")
    @PostMapping("/user/products/add")
    public String addProduct(@ModelAttribute(value = "productinfo") ProductInfo productInfo, Model model){
    //public String addProduct(Model model, @PathVariable("productName") String productName, @PathVariable("productUrl") String productUrl,@PathVariable("productScore") double productScore,@PathVariable("productCategory") String prodcutCategory,@PathVariable("productBottom") double productBottom,@PathVariable("productImageUrl") String productImageUrl,@PathVariable("productId") String productId){
        /*ProductInfo productInfo=new ProductInfo();
        productInfo.setProductName(productName);
        productInfo.setProductUrl(productUrl);
        productInfo.setProductScore(productScore);
        productInfo.setProductCategory(prodcutCategory);
        productInfo.setProductBottom(productBottom);
        productInfo.setProductImageUrl(productImageUrl);
        productInfo.setProductId(productId);*/

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