package pl.lazyteam.pricebreaker.controllers;

import com.google.gson.JsonObject;
import org.springframework.web.bind.annotation.*;
import pl.lazyteam.pricebreaker.crawler.ShopCrawler;
import pl.lazyteam.pricebreaker.crawler.shops.ShopInfo;
import pl.lazyteam.pricebreaker.crawler.shops.WebShop;
import pl.lazyteam.pricebreaker.entity.ProductInfo;

import java.util.List;

@RestController
public class CrawlerController {

    @RequestMapping("/crawler")
    public String crawl(){
        ShopInfo shopInfo=new ShopInfo("Ceneo","https://www.ceneo.pl",
                "https://www.ceneo.pl/;szukaj-","",
                "div[data-pid]");//
        WebShop webShop=new WebShop(shopInfo);
        ShopCrawler shopCrawler=new ShopCrawler(webShop,"Pan Tadeusz",2);
        JsonObject jsonObject=shopCrawler.getResult();
        return jsonObject.toString();
    }

    @RequestMapping("/search/{product}")
    public List<ProductInfo> crawlProduct(String product){
        ShopInfo shopInfo=new ShopInfo("Ceneo",
                "https://www.ceneo.pl",
                "https://www.ceneo.pl/;szukaj-","",
                "div[data-pid]");//
        WebShop webShop=new WebShop(shopInfo);
        ShopCrawler shopCrawler=new ShopCrawler(webShop,product,2);

        return shopCrawler.getProductsList();
    }


}
