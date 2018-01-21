package pl.lazyteam.pricebreaker.form;

public class FlagEditForm
{
    private boolean price_lowers;
    private double priceChange;

    public boolean isPrice_lowers()
    {
        return price_lowers;
    }

    public void setPrice_lowers(boolean price_lowers)
    {
        this.price_lowers = price_lowers;
    }

    public double getPriceChange()
    {
        return priceChange;
    }

    public void setPriceChange(double priceChange)
    {
        this.priceChange = priceChange;
    }
}
