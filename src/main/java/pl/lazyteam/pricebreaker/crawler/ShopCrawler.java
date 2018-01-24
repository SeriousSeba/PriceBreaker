package pl.lazyteam.pricebreaker.crawler;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import pl.lazyteam.pricebreaker.entity.ProductInfo;
import pl.lazyteam.pricebreaker.crawler.shops.WebShop;
import pl.lazyteam.pricebreaker.crawler.utils.JsonUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Class responsible for Crawler configuration and use.
 * Contains functions passing Crawlers search results
 * to Components which are using his abilities.
 */
public class ShopCrawler {

    /**
     * Shop reference
     */
    private WebShop webShop;
    /**
     * Searched product name
     */
    private String product;
    /**
     * Limit number
     */
    private int limit;

    /**
     * Defaul Crawler constructor passing infromations about certain product,
     * web shop configuration and products limit
     * @param webShop Class containg web shop infromation
     * @param product Desired product
     * @param limit Limit of iterations made on shop page
     */
    public ShopCrawler(WebShop webShop, String product, int limit){
        this.webShop=webShop;
        this.product=product;
        this.limit=limit;
    }


    /**
     * Helper function returning Crawler result in Json format
     * @return Json object with products
     */
    public JsonObject getResult(){
        ProductInfo[] productInfos = webShop.getProducts(product,limit);
        JsonObject jsonObject=new JsonObject();
        JsonArray jsonArray=new JsonArray();
        for(ProductInfo productInfo:productInfos) {
            jsonArray.add(JsonUtils.stashProductInfo(productInfo));
        }
        jsonObject.add("array",jsonArray);
        return jsonObject;
    }


    /**
     * Function returning Crawler result in arra of database entities
     * @return Array with products info
     */
    public List<ProductInfo> getProductsList(){
        ProductInfo[] productInfos = webShop.getProducts(product,limit);
        return Arrays.asList(productInfos);
    }




}


