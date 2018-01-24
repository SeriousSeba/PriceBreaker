package pl.lazyteam.pricebreaker.crawler.shops;

import com.sun.deploy.net.URLEncoder;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pl.lazyteam.pricebreaker.entity.ProductInfo;

import java.io.IOException;
import java.util.LinkedList;

/**
 * Shop configuration Class containging functions responsible
 * for getting certain informations from shop url.
 * All operations are working on html response from shop page
 */
public class WebShop {
    /**
     * Shop confugration class reference
     */
    private ShopInfo shopInfo;

    /**
     * Default constructor with infromations about shop html elemtns
     * @param shopInfo Class with shop page informations
     */
    public WebShop(ShopInfo shopInfo){
        this.shopInfo=shopInfo;
    }


    /**
     * Function returning all products with given name on web page main search
     * engine. Limit is used for stop condition.
     * @param productName Searched product name
     * @param limit Limit of iteretions made on page
     * @return Array of products info
     */
    public ProductInfo[] getProducts(String productName, int limit) {
        String siteURL = parseProductName(productName);
        System.out.println(siteURL);
        LinkedList<ProductInfo> productInfos=new LinkedList<ProductInfo>();
        PageInfo pageInfo;
        for(int i=0;i<limit && siteURL!=null;i++){
            pageInfo=getPageInfo(siteURL);
            if(pageInfo==null)
                break;
            siteURL=pageInfo.getNextSiteURL();
            ProductInfo[] tmpInfos=pageInfo.getProductInfos();
            for(ProductInfo productInfo:tmpInfos) {
                productInfos.addLast(productInfo);
            }
        }

        ProductInfo[] tab=new ProductInfo[productInfos.size()];
        productInfos.toArray(tab);
        return tab;
    }

    /**
     * Helper function for getting product info from html element
     * @param element Html element representing data row
     * @return Class containing all product informations
     */
    private ProductInfo getProductInfo(Element element){
        ProductInfo productInfo=shopInfo.getProductInfo(element);
        return productInfo;
    }

    /**
     * Parses product name for url style
     * @param productName Given product
     * @return Url representation of product name
     */
    private String parseProductName(String productName){
        String newProductName=productName.replaceAll("[ ]+","+");
        return shopInfo.getSearchURL()+newProductName+shopInfo.getSearchURLSuffix();
    }

    /**
     * Returns information class about automaticly generated page from shop page.
     * Convienient for further work on this page data and iterating through
     * whole search result pages.
     * @param siteURL Url of specific search page
     * @return Class containing all html page informations
     */
    private PageInfo getPageInfo(String siteURL){
        Document document;
        Elements elements;
        try {
            document = getSiteDocument(siteURL);
            elements=document.select(shopInfo.getHrefs());
        } catch (IOException e) {
            return null;
        }
        LinkedList<ProductInfo> infos=new LinkedList<ProductInfo>();


        for(Element element:elements){
            infos.addLast(
                    getProductInfo(element)
            );
        }

        PageInfo pageInfo=new PageInfo();
        pageInfo.setNextSiteURL(
                shopInfo.getUrl() + getNextSiteLink(document)
        );

        ProductInfo[] productInfos=new ProductInfo[infos.size()];
        infos.toArray(productInfos);
        pageInfo.setProductInfos(productInfos);

        return pageInfo;
    }

    /**
     * Function searches for next page reference from document representing
     * certain page source html
     * @param document Html page representation
     * @return Next site url
     */
    private String getNextSiteLink(Document document){
        String site;
        Element button = document.select("[rel=next]").first();
        if(button==null)
            return null;
        site=button.attr("href");
        return site;
    }

    /**
     * Function returns html source of page represented by string url
     * @param siteURL Url of given web page
     * @return Html source representation
     * @throws IOException Exception if connection was closed or not initalized proerly
     */
    private Document getSiteDocument(String siteURL) throws IOException {
        Connection.Response response=Jsoup.connect(siteURL).
                method(Connection.Method.GET).
                timeout(10000).
                execute();
        Document document=Jsoup.parse(response.body());
        return document;
    }

//    private ProductInfo[] validateProducts(ProductInfo[] productInfos,String productName) {
//        LinkedList<ProductInfo> resultList=new LinkedList<ProductInfo>();
//        StringBuilder regexBuilder=new StringBuilder();
//        regexBuilder.append(".*");
//        for(String part:productName.split(" "))
//            regexBuilder.append(part+".*");
//        String regex=regexBuilder.toString();
//        for(ProductInfo productInfo:productInfos) {
//            if(productInfo.getProductName().matches(regex))
//                resultList.addLast(productInfo);
//        }
//        ProductInfo[] result=new ProductInfo[resultList.size()];
//        resultList.toArray(result);
//        return result;
//    }


}





