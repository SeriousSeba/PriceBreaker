package pl.lazyteam.pricebreaker.entity;

import javax.persistence.*;

@Entity
public class DefaultUserFlags
{
    @Id
    private int user_id;

    private double priceChange;

    private boolean price_lowers;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public DefaultUserFlags()
    {
        super();
    }

    public DefaultUserFlags(User user, double priceChange, boolean price_lowers)
    {
        this.priceChange = priceChange;
        this.user = user;
        this.price_lowers = price_lowers;
        this.user_id = user.getUser_id();
    }
    public DefaultUserFlags(User user)
    {
        this.user = user;
        this.priceChange = 0.2;
        this.price_lowers = true;
        this.user_id = user.getUser_id();
    }

    public int getUser_id()
    {
        return user_id;
    }

    public void setUser_id(int user_id)
    {
        this.user_id = user_id;
    }

    public double getPriceChange()
    {
        return priceChange;
    }

    public void setPriceChange(double priceChange)
    {
        this.priceChange = priceChange;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
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
