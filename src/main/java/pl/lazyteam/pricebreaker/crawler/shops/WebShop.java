package pl.lazyteam.pricebreaker.crawler.shops;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pl.lazyteam.pricebreaker.entity.ProductInfo;

import java.io.IOException;
import java.util.LinkedList;


public class WebShop {
    ShopInfo shopInfo;

    public WebShop(ShopInfo shopInfo){
        this.shopInfo=shopInfo;
    }

    public LinkedList<String> getShopsList(ProductInfo productInfo){
        String site=productInfo.getProductUrl();
        Document document=null;
        try {
            document=getSiteDocument(site);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public ProductInfo[] getProducts(String productName, int limit) {
        String siteURL = parseProductName(productName);
        LinkedList<ProductInfo> productInfos=new LinkedList<ProductInfo>();
        PageInfo pageInfo;
        for(int i=0;i<limit && siteURL!=null;i++){
            pageInfo=getPageInfo(siteURL);
            if(pageInfo==null)
                break;
            siteURL=pageInfo.getNextSiteURL();
            ProductInfo[] tmpInfos=pageInfo.getProductInfos();
            //validateProducts(pageInfo.getProductInfos(),productName);
            for(ProductInfo productInfo:tmpInfos) {
                productInfos.addLast(productInfo);
            }
        }

        ProductInfo[] tab=new ProductInfo[productInfos.size()];
        productInfos.toArray(tab);
        return tab;
////////////////////////////////////
    }

    private ProductInfo getProductInfo(Element element){
        ProductInfo productInfo=shopInfo.getProductInfo(element);
        return productInfo;
    }

    private String parseProductName(String productName){
        String newProductName=productName.replaceAll("[ ]+","+");
        return shopInfo.getSearchURL()+newProductName+shopInfo.getSearchURLSuffix();
    }

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

    private String getNextSiteLink(Document document){
        String site;
        Element button = document.select("[rel=next]").first();
        if(button==null)
            return null;
        site=button.attr("href");
        return site;
    }

    private Document getSiteDocument(String siteURL) throws IOException {
        Connection.Response response=Jsoup.connect(siteURL).
                method(Connection.Method.GET).
                timeout(10000).
                execute();
        Document document=Jsoup.parse(response.body());
        return document;
    }

    private ProductInfo[] validateProducts(ProductInfo[] productInfos,String productName) {
        LinkedList<ProductInfo> resultList=new LinkedList<ProductInfo>();
        StringBuilder regexBuilder=new StringBuilder();
        regexBuilder.append(".*");
        for(String part:productName.split(" "))
            regexBuilder.append(part+".*");
        String regex=regexBuilder.toString();
        for(ProductInfo productInfo:productInfos) {
            if(productInfo.getProductName().matches(regex))
                resultList.addLast(productInfo);
        }
        ProductInfo[] result=new ProductInfo[resultList.size()];
        resultList.toArray(result);
        return result;
    }


}





