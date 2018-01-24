package pl.lazyteam.pricebreaker.crawler.shops;

import org.jsoup.nodes.Element;
import pl.lazyteam.pricebreaker.entity.ProductInfo;

/**
 * Class representing shop informations some for representing directly
 * to user other for Crawler search algorithms
 */
public class ShopInfo {

    /**
     * Shop name
     */
    private String name;
    /**
     * Shop main url
     */
    private String url;
    /**
     * Shop search engine url
     */
    private String searchURL;
    /**
     * Shop search engine suffix
     */
    private String searchURLSuffix;
    /**
     * Shop enginge html element representation of products
     */
    private String hrefs;


    /**
     * @param name
     * @param url
     * @param searchURL
     * @param searchURLSuffix
     * @param hrefs
     */
    public ShopInfo(String name, String url, String searchURL, String searchURLSuffix, String hrefs){
        this.name=name;
        this.url=url;
        this.searchURL=searchURL;
        this.searchURLSuffix=searchURLSuffix;
        this.hrefs=hrefs;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return
     */
    public String getSearchURL() {
        return searchURL;
    }

    /**
     *
     * @return
     */
    public String getSearchURLSuffix() {
        return searchURLSuffix;
    }

    /**
     *
     * @return
     */
    public String getHrefs() {
        return hrefs;
    }

    /**
     *
     * @return
     */
    public String getUrl() {
        return url;
    }

    /**
     * Returns whole product info class from html element
     * @param element Html representation of result row in page
     * @return Representation class of product infromations
     */
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

        try {
            urlI = urlI.split("/")[5];
        }
        catch (Exception e){
            urlI=null;
        }

        if(urlI!=null)
        productInfo.setProductUrl(
               url + "/" +urlI
        );

        else

            productInfo.setProductUrl(
                    element.select("a[href]").first().attr("href")
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
