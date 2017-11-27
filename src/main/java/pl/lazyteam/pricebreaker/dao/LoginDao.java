package pl.lazyteam.pricebreaker.dao;

import pl.lazyteam.pricebreaker.Entity.User;
import java.util.List;


public interface LoginDao
{
    User findUser(String username);

    List<String> getUserRoles(String username);

}
