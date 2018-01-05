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

        productInfo.setProductImageUrl(
                element.select("img[data-src]").first().attr("data-src")
        );

        productInfo.setProductUrl(
               url + element.select("a[href]").first().attr("href")
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

        productInfo.setProductCategory(
                element.select("img[alt]").attr("alt")
        );

        return productInfo;
    }







}
