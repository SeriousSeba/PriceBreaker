package pl.lazyteam.pricebreaker.crawler.products;

public class ProductInfo {
    private String productImageUrl;
    private String productUrl;
    private String productName;
    private double productScore;
    private String productId;
    private double productBottom;
    private String prodcutCategory;


    public String getproductImageUrl() { return productImageUrl; }

    public void setProductImageUrl(String imageUrl) {
        this.productImageUrl = imageUrl;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductScore() {
        return productScore;
    }

    public void setProductScore(double productScore) {
        this.productScore = productScore;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public double getProductBottom() {
        return productBottom;
    }

    public void setProductBottom(double productBottom) {
        this.productBottom = productBottom;
    }


    public String getProdcutCategory() { return prodcutCategory; }

    public void setProdcutCategory(String prodcutCategory) { this.prodcutCategory = prodcutCategory; }

    public String getInfo(){
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Nazwa: " + getProductName() + "\n");
            stringBuilder.append("Id: " + getProductId() + "\n");
            stringBuilder.append("Kategoria: " + getProdcutCategory() + "\n");
            stringBuilder.append("Ocena: " + getProductScore() + "\n");
            stringBuilder.append("URL: " + getProductUrl() + "\n");
            stringBuilder.append("ImageURL: " + getproductImageUrl() + "\n");
            stringBuilder.append("Najnizsza cena: " + getProductBottom() + "\n\n\n");
            return stringBuilder.toString();

    }

}
