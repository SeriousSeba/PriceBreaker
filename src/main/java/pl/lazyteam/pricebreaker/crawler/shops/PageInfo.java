package pl.lazyteam.pricebreaker.crawler.shops;


import pl.lazyteam.pricebreaker.entity.ProductInfo;

/**
 * Class representing infromations found on single page
 * from shop search engine
 */
public class PageInfo {
    /**
     * Array of all products found in this page
     */
    private ProductInfo[] productInfos;
    /**
     * Url to next page site
     */
    private String nextSiteURL;

    /**
     *
     * @return
     */
    public ProductInfo[] getProductInfos() {
        return productInfos;
    }

    /**
     *
     * @param productInfos
     */
    public void setProductInfos(ProductInfo[] productInfos) {
        this.productInfos = productInfos;
    }

    /**
     *
     * @return
     */
    public String getNextSiteURL() {
        return nextSiteURL;
    }

    /**
     *
     * @param nextSiteURL
     */
    public void setNextSiteURL(String nextSiteURL) {
        this.nextSiteURL = nextSiteURL;
    }

}
