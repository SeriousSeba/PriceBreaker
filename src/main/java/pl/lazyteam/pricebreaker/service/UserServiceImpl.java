package pl.lazyteam.pricebreaker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.lazyteam.pricebreaker.dao.TokenDao;
import pl.lazyteam.pricebreaker.entity.User;
import pl.lazyteam.pricebreaker.entity.UserRole;
import pl.lazyteam.pricebreaker.dao.UserDao;
import pl.lazyteam.pricebreaker.entity.VerificationToken;

import java.util.Calendar;
import java.util.List;

@Service
public class UserServiceImpl implements UserService
{
    @Autowired
    UserDao userDao;

    @Autowired
    TokenDao tokenDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static final String TOKEN_INVALID = "invalidToken";
    public static final String TOKEN_EXPIRED = "expired";
    public static final String TOKEN_VALID = "valid";


    public List<User> list()
    {
        return userDao.findAll();
    }

    public User findUserByUsername(String username)
    {
        return userDao.findByUsername(username).get(0);
    }

    public boolean validatePassword(String password)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String encPassword = findUserByUsername(auth.getName()).getPassword();
        if (BCrypt.checkpw(password,encPassword))
            return true;
        return false;
    }

    @Override
    public void delete(String username)
    {
        userDao.deleteByUsername(username);
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


    @Override
    public VerificationToken getVerificationToken(String verificationToken)
    {
        return tokenDao.findVerificationTokenByToken(verificationToken);
    }

    @Override
    public void createVerificationTokenForUser(final User user, final String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        tokenDao.save(myToken);
    }

    @Override
    public String validateVerificationToken(String token)
    {
        VerificationToken verificationToken = tokenDao.findVerificationTokenByToken(token);
        if (verificationToken == null)
        {
            return TOKEN_INVALID;
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0)
        {
            tokenDao.delete(verificationToken);
            return TOKEN_EXPIRED;
        }

        user.setEnabled(true);
        userDao.save(user);
        return TOKEN_VALID;
    }

    public void updateUser(User user){
        userDao.save(user);
    }

    public boolean userExists(String username)
    {
        if (userDao.findByUsername(username).size() > 0)
            return true;
        return false;
    }
}
