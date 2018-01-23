package pl.lazyteam.pricebreaker.crawler.shops;

import org.jsoup.nodes.Element;
import pl.lazyteam.pricebreaker.entity.ProductInfo;


public class ShopInfo {

    private String name;
    private String url;
    private String searchURL;
    private String searchURLSuffix;
    private String hrefs;


    public ShopInfo(String name, String url, String searchURL, String searchURLSuffix, String hrefs){
        this.name=name;
        this.url=url;
        this.searchURL=searchURL;
        this.searchURLSuffix=searchURLSuffix;
        this.hrefs=hrefs;
    }

    public String getName() {
        return name;
    }

    public String getSearchURL() {
        return searchURL;
    }

    public String getSearchURLSuffix() {
        return searchURLSuffix;
    }

    public String getHrefs() {
        return hrefs;
    }

    public String getUrl() {
        return url;
    }

    public ProductInfo getProductInfo(Element element) {
        ProductInfo productInfo=new ProductInfo();
        Element tmp;

        productInfo.setProductId(
                element.attr("data-pid")
        );

        tmp=element.select("span.score*").first();
        if(tmp!=null)
            productInfo.setProductScore(
                    Double.parseDouble(
                            element.select("span.score*").first().text().replace(",",".")
                    )
            );
        else
            productInfo.setProductScore(0);

        String urlI=element.select("img[data-src]").first().attr("data-src");
        productInfo.setProductImageUrl(
               urlI
        );

        urlI=urlI.split("/")[5];

        productInfo.setProductUrl(
               url + "/" +urlI
        );

        productInfo.setProductName(
                element.select("div.list-prod-name").first().text()
        );

        productInfo.setProductBottom(
                Double.parseDouble(
                        element.select("span.price-int").first().text()+
                                element.select("span.price-fraction").first().text().replace(",",".")
                )
        );

        try{
            productInfo.setProductCategory(
                    element.select(".subcat-type-icon").first().select("img").first().attr("alt")
            );
        }
        catch (Exception e){
            e.printStackTrace();
            productInfo.setProductCategory("Brak");
        }


        productInfo.setStocks(
                element.select(".listing-shops-number").first().text()
        );

        return productInfo;
    }







}
