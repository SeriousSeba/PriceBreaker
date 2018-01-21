package pl.lazyteam.pricebreaker.entity;

import javax.persistence.*;

@Entity
public class ProductFlags
{
    @Id
    private Long id;

    private boolean price_lowers;

    private double priceChange;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, name = "id")
    private ProductInfo product;

    public ProductFlags()
    {
        super();
    }

    public ProductFlags(ProductInfo product, double priceChange, boolean price_lowers)
    {
        this.priceChange = priceChange;
        this.product = product;
        this.price_lowers = price_lowers;
        this.id = product.getId();
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public double getPriceChange()
    {
        return priceChange;
    }

    public void setPriceChange(double priceChange)
    {
        this.priceChange = priceChange;
    }

    public ProductInfo getProduct()
    {
        return product;
    }

    public void setProduct(ProductInfo product)
    {
        this.product = product;
    }

    public boolean isPrice_lowers()
    {
        return price_lowers;
    }

    public void setPrice_lowers(boolean price_lowers)
    {
        this.price_lowers = price_lowers;
    }
}
