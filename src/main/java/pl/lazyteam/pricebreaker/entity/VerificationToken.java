package pl.lazyteam.pricebreaker.entity;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
public class VerificationToken
{
    private static final int EXPIRATION = 60 * 24;

    @Id
    private int user_id;

    private String token;

    //@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private Date expiryDate;

    public VerificationToken()
    {
        super();
    }

    public VerificationToken(String token, User user)
    {
        this.token = token;
        this.user = user;
        this.user_id = user.getUser_id();
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }


    private Date calculateExpiryDate(final int expiryTimeInMinutes)
    {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    public int getUser_id()
    {
        return user_id;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public Date getExpiryDate()
    {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate)
    {
        this.expiryDate = expiryDate;
    }
}
