package pl.lazyteam.pricebreaker.entity;

import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "products")
public class ProductInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String productImageUrl;
    private String productUrl;
    private String productName;
    private double productScore;
    private String productId;
    private double productBottom;
    private String productCategory;

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date lastUpdate;

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "products")
    private Set<User> users = new HashSet<>();


    public String getProductImageUrl() { return productImageUrl; }

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


    public String getProductCategory() { return productCategory; }

    public void setProductCategory(String prodcutCategory) { this.productCategory = prodcutCategory; }

    public void getInfo(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Nazwa: " + getProductName() + "\n");
        stringBuilder.append("Id: " + getProductId() + "\n");
        stringBuilder.append("Kategoria: " + getProductCategory() + "\n");
        stringBuilder.append("Ocena: " + getProductScore() + "\n");
        stringBuilder.append("URL: " + getProductUrl() + "\n");
        stringBuilder.append("ImageURL: " + getProductImageUrl() + "\n");
        stringBuilder.append("Najnizsza cena: " + getProductBottom() + "\n\n\n");
        System.out.println(stringBuilder.toString());

    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}