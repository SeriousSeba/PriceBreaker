package pl.lazyteam.pricebreaker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.lazyteam.pricebreaker.Entity.User;
import pl.lazyteam.pricebreaker.Entity.UserRole;
import pl.lazyteam.pricebreaker.dao.UserDao;

import java.util.List;

@Service
public class UserServiceImpl implements UserService
{
    @Autowired
    UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public List<User> list()
    {
        return userDao.findAll();
    }

    public User findUserByUsername(String username)
    {
        return userDao.findByUsername(username).get(0);
    }

    @Override
    public boolean emailExists(String email)
    {
        if (userDao.findByEmail(email).size() > 0)
            return true;
        return false;
    }

    public void update(String username, String password)
    {
        User user = findUserByUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        userDao.save(user);
    }

    public void add(String username, String password, String email)
    {
        User user = new User(username, passwordEncoder.encode(password), email, new UserRole(username, "ROLE_USER"));
        userDao.save(user);
    }

    public boolean userExists(String username)
    {
        if (userDao.findByUsername(username).size() > 0)
            return true;
        return false;
    }
}
