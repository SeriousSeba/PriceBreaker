package pl.lazyteam.pricebreaker.crawler.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import pl.lazyteam.pricebreaker.entity.ProductInfo;

/**
 * Helper class for working on Json resources.
 * Convienient for future front end development
 */
public class JsonUtils {

    /**
     * Convers given ProductInfo class to Json object
     * @param productInfo ProductInfo to be converted
     * @return New Json object with infromations about product
     */
    public static JsonObject stashProductInfo(ProductInfo productInfo){
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("ProductName",productInfo.getProductName());
        jsonObject.addProperty("ProductURL",productInfo.getProductUrl());
        jsonObject.addProperty("ProductCategory",productInfo.getProductCategory());
        jsonObject.addProperty("ProductId",productInfo.getProductId());
        jsonObject.addProperty("ImageURL",productInfo.getProductImageUrl());
        jsonObject.addProperty("ProductBottom",productInfo.getProductBottom());
        jsonObject.addProperty("ProductScore",productInfo.getProductScore());
        return jsonObject;
    }

    /**
     * Converts Json object to ProductInfo entity
     * @param jsonObject Json object to be converted
     * @return ProductInfo entity
     */
    public static ProductInfo getProductInfo(JsonObject jsonObject){
        ProductInfo productInfo=new ProductInfo();
        String result;
        JsonElement element;

        element=jsonObject.get("ProductName");
        result = element==null || element.isJsonNull()?
                null: element.getAsString();
        productInfo.setProductName(result);

        element=jsonObject.get("ProductURL");
        result = element==null || element.isJsonNull()?
                null: element.getAsString();
        productInfo.setProductUrl(result);

        element=jsonObject.get("ProductCategory");
        result = element==null || element.isJsonNull()?
                null: element.getAsString();
        productInfo.setProductCategory(result);

        element=jsonObject.get("ProductId");
        result = element==null || element.isJsonNull()?
                null: element.getAsString();
        productInfo.setProductId(result);

        element=jsonObject.get("ImageURL");
        result = element==null || element.isJsonNull()?
                null: element.getAsString();
        productInfo.setProductImageUrl(result);

        double result2;

        element=jsonObject.get("ProductBottom");
        result2 = element==null || element.isJsonNull()?
                0.0 : element.getAsDouble();

        if(result2!=0.0)
            productInfo.setProductBottom(result2);

        element=jsonObject.get("ProductScore");
        result2 = element==null || element.isJsonNull()?
                0.0: element.getAsDouble();
        if(result2!=0.0)
            productInfo.setProductScore(result2);

        return productInfo;
    }

}
