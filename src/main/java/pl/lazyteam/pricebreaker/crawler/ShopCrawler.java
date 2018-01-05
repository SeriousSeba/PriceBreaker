package pl.lazyteam.pricebreaker.crawler;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.thymeleaf.expression.Lists;
import pl.lazyteam.pricebreaker.entity.ProductInfo;
import pl.lazyteam.pricebreaker.crawler.shops.WebShop;
import pl.lazyteam.pricebreaker.crawler.utils.JsonUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class ShopCrawler {

    private WebShop webShop;
    private String product;
    private int limit;
    private List<ProductInfo> list=new LinkedList<ProductInfo>();

    public ShopCrawler(WebShop webShop, String product, int limit){

        this.webShop=webShop;
        this.product=product;
        this.limit=limit;
    }




    public void run(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ProductInfo[] productInfos = webShop.getProducts(product,limit);
                for(ProductInfo productInfo:productInfos) {
                    LinkedList<String> list = webShop.getShopsList(productInfo);
                    productInfo.getInfo();
                }
            }
        }
        ).start();
    }

    public JsonObject getResult(){
        ProductInfo[] productInfos = webShop.getProducts(product,limit);
        JsonObject jsonObject=new JsonObject();
        JsonArray jsonArray=new JsonArray();
        for(ProductInfo productInfo:productInfos) {
            LinkedList<String> list = webShop.getShopsList(productInfo);
            //stringBuilder.append(productInfo.getInfo());
            jsonArray.add(JsonUtils.stashProductInfo(productInfo));
        }
        jsonObject.add("array",jsonArray);
        return jsonObject;
    }


    public List<ProductInfo> getProductsList(){
        ProductInfo[] productInfos = webShop.getProducts(product,limit);
        return Arrays.asList(productInfos);
    }




}


