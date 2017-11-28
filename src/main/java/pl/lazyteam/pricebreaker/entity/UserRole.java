package pl.lazyteam.pricebreaker.entity;

import javax.persistence.*;

@Entity
@Table(name = "user_roles")
public class UserRole
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int user_id;
    private String username;
    private String user_role;


    public UserRole(){}

    public UserRole(String username, String user_role)
    {
        this.username = username;
        this.user_role = user_role;
    }


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

    public String getUser_role()
    {
        return user_role;
    }

    public void setUser_role(String user_role)
    {
        this.user_role = user_role;
    }
}
