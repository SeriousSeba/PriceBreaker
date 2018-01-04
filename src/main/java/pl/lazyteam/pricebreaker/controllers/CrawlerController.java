package pl.lazyteam.pricebreaker.controllers;

import com.google.gson.JsonObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lazyteam.pricebreaker.crawler.ShopCrawler;
import pl.lazyteam.pricebreaker.crawler.shops.ShopInfo;
import pl.lazyteam.pricebreaker.crawler.shops.WebShop;

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


}
