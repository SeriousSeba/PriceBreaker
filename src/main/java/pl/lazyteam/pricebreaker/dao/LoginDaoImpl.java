package pl.lazyteam.pricebreaker.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import pl.lazyteam.pricebreaker.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of LoginDAO
 */
@Component
public class LoginDaoImpl implements  LoginDao {

    @Autowired
    UserDao userDao;

    /**
     * Method allows to find user by his username
     * @param username username
     * @return object User
     */
    @Override
    public User findUser(String username) {
        List<User> list = userDao.findByUsername(username);
        if (list.size() > 0 ) {
            return list.get(0);
        }
        else{
            throw new UsernameNotFoundException("username was not found");
        }
    }

    /**
     * Method that allows to get user role(admin,user)
     * @param username username
     * @return list of roles
     */
    @Override
    public List<String> getUserRoles(String username) {
        List<User> list = userDao.findByUsername(username);
        if (list.size() > 0 ) {
            List<String> roles = new ArrayList<String>();
            roles.add(list.get(0).getUserRole().getUser_role());
            return roles;
        }
        else{
            throw new UsernameNotFoundException("username was not found");
        }
    }

}
