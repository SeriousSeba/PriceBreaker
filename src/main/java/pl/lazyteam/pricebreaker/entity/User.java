package pl.lazyteam.pricebreaker.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name="users")
public class User
{
    @Id
    @GeneratedValue(generator = "newGenerator")
    @GenericGenerator(name = "newGenerator", strategy = "foreign", parameters = { @org.hibernate.annotations.Parameter(value = "userRole", name = "property")})
    private int user_id;
    private String username;
    private String password;

        @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private UserRole userRole;

    public UserRole getUserRole()
    {
        return userRole;
    }

    public void setUserRole(UserRole userRole)
    {
        this.userRole = userRole;
    }

    public User(){};
    public User(String username, String password, String email, UserRole userRole)
    {
        this.username = username;
        this.password = password;
        this.email = email;
        this.userRole = userRole;
    }

    private String email;

    public int getUser_id()
    {
        return user_id;
    }

    public void setUser_id(int user_id)
    {
        this.user_id = user_id;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }
}