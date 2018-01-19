package pl.lazyteam.pricebreaker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.WebContext;
import pl.lazyteam.pricebreaker.crawler.ShopCrawler;
import pl.lazyteam.pricebreaker.crawler.shops.ShopInfo;
import pl.lazyteam.pricebreaker.crawler.shops.WebShop;
import pl.lazyteam.pricebreaker.dao.ProductDAO;
import pl.lazyteam.pricebreaker.entity.ProductInfo;
import pl.lazyteam.pricebreaker.form.SearchForm;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
public class ProductController {
    @Autowired
    ProductDAO productDAO;

    @GetMapping("products/{id}")
    public ProductInfo getProductById(@PathVariable(value="id") Long id){
        ProductInfo productInfo=productDAO.findOne(id);
        return productInfo;
    }

    @GetMapping("/products/all")
    public List<ProductInfo> getAllProducts(Model model){
        if (model != null)
            model.addAttribute("productsList", productDAO.findAll());
        return productDAO.findAll();
    }

    @GetMapping("/products/delete/{id}")
    public String delete(@PathVariable(value="id") Long id){
        productDAO.delete(id);
        return "redirect:/products/all";
    }

    @PostMapping("products/add")
    public ProductInfo add(@Valid @RequestBody ProductInfo productInfo){
        return productDAO.save(productInfo);
    }



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
