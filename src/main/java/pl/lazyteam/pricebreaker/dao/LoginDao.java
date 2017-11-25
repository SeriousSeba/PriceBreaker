package pl.lazyteam.pricebreaker.dao;

import pl.lazyteam.pricebreaker.Entity.User;

import java.util.List;

//TODO
public interface LoginDao
{
    User findClient(String username);

    List<String> getUserRoles(String username);

}
