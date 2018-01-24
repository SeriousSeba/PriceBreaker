package pl.lazyteam.pricebreaker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.WebContext;
import pl.lazyteam.pricebreaker.crawler.ShopCrawler;
import pl.lazyteam.pricebreaker.crawler.products.ProductUpdater;
import pl.lazyteam.pricebreaker.crawler.shops.ShopInfo;
import pl.lazyteam.pricebreaker.crawler.shops.WebShop;
import pl.lazyteam.pricebreaker.dao.DefaultFlagsDao;
import pl.lazyteam.pricebreaker.dao.ProductDAO;
import pl.lazyteam.pricebreaker.dao.ProductFlagsDao;
import pl.lazyteam.pricebreaker.dao.UserDao;
import pl.lazyteam.pricebreaker.entity.ProductFlags;
import pl.lazyteam.pricebreaker.entity.ProductInfo;
import pl.lazyteam.pricebreaker.entity.User;
import pl.lazyteam.pricebreaker.form.SearchForm;
import pl.lazyteam.pricebreaker.service.UserService;

import javax.validation.Valid;
import java.util.*;

/**
 * ProductController is a class that allows users to manage theirs products. They can add, delete or edit positions on theirs list.
 */
@Controller
public class ProductController {
    @Autowired
    ProductDAO productDAO;

    @Autowired
    UserService userService;

    /**
     * Method that allows to get ProductInfo object which contains information like URL, rating, image, price of product.
     * @param id product id
     * @return ProductInfo object with information about products
     */
    @GetMapping("products/{id}")
    public ProductInfo getProductById(@PathVariable(value="id") Long id){
        ProductInfo productInfo=productDAO.findOne(id);
        return productInfo;
    }

    /**
     * Method that allows to get list of all products in database.
     * @param model holder for model attributes
     * @return list of ProductInfo
     */
    @GetMapping("/products/all")
    public List<ProductInfo> getAllProducts(Model model){
        if (model != null)
            model.addAttribute("productsList", productDAO.findAll());
        return productDAO.findAll();
    }

    /**
     * Method that allows user to delete product from his list.
     * @param id product id
     * @return redirect path to list of products
     */
    @GetMapping("/products/delete/{id}")
    public String delete(@PathVariable(value="id") Long id){
        ProductInfo productInfo=productDAO.getOne(id);
        Set<User> set=productInfo.getUsers();
        for(User user:set){
            user.getProducts().remove(productInfo);
        }
        productDAO.delete(id);

        return "redirect:/products/all";
    }

    /**
     * Method allows to update all products in database.
     * @return redirect path to list of products
     */
    @GetMapping("/products/update")
    public String update(){
        List<ProductInfo>productInfos=productDAO.findAll();
        for(ProductInfo productInfo:productInfos) {
            if(!ProductUpdater.updateProduct(productInfo))
                productDAO.delete(productInfo.getId());
            productInfo.setLastUpdate(new Date());
            productDAO.save(productInfo);
        }
        return "redirect:/products/all";
    }


    /**
     * Method allows to add new product to database.
     * @param productInfo object ProductInfo with information about product
     * @return ProductInfo object which was added to database
     */
    @PostMapping("products/add")
    public ProductInfo add(@Valid @RequestBody ProductInfo productInfo){ return productDAO.save(productInfo); }


    /**
     * Method that allows to search products. It add search form to page.
     * @param model holder for model attributes
     * @param error String with information about error
     * @param product name of product to search
     * @return
     */
    @GetMapping("/search")
    public String searchForm(Model model, @RequestParam(value="error", required = false) String error, @RequestParam(value="product", required = false) String product){

        model.addAttribute("searchForm", new SearchForm());
        if(error != null){
            model.addAttribute("serachError", true);
        }
        if (false)
        {
            WebContext context = new org.thymeleaf.context.WebContext(null, null, null);
            context.setVariable("searchForm", new SearchForm());
        }

        if(product != null){
            ShopInfo shopInfo=new ShopInfo("Ceneo",
                    "https://www.ceneo.pl",
                    "https://www.ceneo.pl/;szukaj-","",
                    "div[data-pid]");//
            WebShop webShop=new WebShop(shopInfo);
            ShopCrawler shopCrawler=new ShopCrawler(webShop,product,2);
            List<ProductInfo> productList=shopCrawler.getProductsList();
            Collections.sort(productList, new Comparator<ProductInfo>() {
                @Override
                public int compare(ProductInfo o1, ProductInfo o2) {
                    Double price1=o1.getProductBottom();
                    Double price2=o2.getProductBottom();
                    return price1.compareTo(price2);
                }
            });
            model.addAttribute("productsList", productList);
            model.addAttribute("productinfo", new ProductInfo());
        }
        return "/search";
    }

    /**
     * Method that allows to search products. It run crawler and get information from shops about searched products.
     * @param model holder for model attributes
     * @param error String with information about error
     * @param product product name of product to search
     * @return
     */
    @GetMapping("/search/{product}")
    public List<ProductInfo> search(Model model, @RequestParam(value="error", required = false) String error, @RequestParam(value="product", required = true) String product){
        ShopInfo shopInfo=new ShopInfo("Ceneo",
                "https://www.ceneo.pl",
                "https://www.ceneo.pl/;szukaj-","",
                "div[data-pid]");//
        WebShop webShop=new WebShop(shopInfo);
        ShopCrawler shopCrawler=new ShopCrawler(webShop,product,2);
        model.addAttribute("productsList", shopCrawler.getProductsList());
        return shopCrawler.getProductsList();
    }

}
