package pl.lazyteam.pricebreaker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.lazyteam.pricebreaker.dao.ProductDAO;
import pl.lazyteam.pricebreaker.dao.ProductFlagsDao;
import pl.lazyteam.pricebreaker.dao.TokenDao;
import pl.lazyteam.pricebreaker.entity.*;
import pl.lazyteam.pricebreaker.dao.UserDao;

import java.util.Calendar;
import java.util.List;

/**
 * Service that manage users.
 */
@Service
public class UserServiceImpl implements UserService
{
    @Autowired
    UserDao userDao;

    @Autowired
    TokenDao tokenDao;

    @Autowired
    ProductFlagsDao productFlagsDao;

    @Autowired
    ProductDAO productDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static final String TOKEN_INVALID = "invalidToken";
    public static final String TOKEN_EXPIRED = "expired";
    public static final String TOKEN_VALID = "valid";


    /**
     * Method that allows to get list of all users.
     * @return list of users
     */
    public List<User> list()
    {
        return userDao.findAll();
    }

    /**
     * Method that allows to find user by his username.
     * @param username username
     * @return User object
     */
    public User findUserByUsername(String username)
    {
        return userDao.findByUsername(username).get(0);
    }

    /**
     * Mathod that allows to valide user password
     * @param password user password
     * @return information about validation
     */
    public boolean validatePassword(String password)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String encPassword = findUserByUsername(auth.getName()).getPassword();
        if (BCrypt.checkpw(password,encPassword))
            return true;
        return false;
    }

    /**
     * Method that allows to delete user
     * @param username username
     */
    @Override
    public void delete(String username)
    {
        userDao.deleteByUsername(username);
    }

    /**
     * Method that allows to check if email exists in database.
     * @param email user email
     * @return
     */
    @Override
    public boolean emailExists(String email)
    {
        if (userDao.findByEmail(email).size() > 0)
            return true;
        return false;
    }

    /**
     * Method that allows to update information about user
     * @param username username
     * @param password password
     */
    public void update(String username, String password)
    {
        User user = findUserByUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        userDao.save(user);
    }

    /**
     * Method that allows to add new user to database.
     * @param username username
     * @param password password
     * @param email email adress
     */
    public void add(String username, String password, String email)
    {
        User user = new User(username, passwordEncoder.encode(password), email, new UserRole(username, "ROLE_USER"));
        userDao.save(user);
    }


    /**
     * Method returns user verification token.
     * @param verificationToken verification token
     * @return VerificationToken object
     */
    @Override
    public VerificationToken getVerificationToken(String verificationToken)
    {
        return tokenDao.findVerificationTokenByToken(verificationToken);
    }

    /**
     * Method that allows to create new verifcation token for user.
     * @param user User object
     * @param token token to generate
     */
    @Override
    public void createVerificationTokenForUser(final User user, final String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        tokenDao.save(myToken);
    }

    /**
     * Method that allows to validate verification token
     * @param token user token
     * @return information about validation
     */
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

    /**
     * Method that allows to update user in databse.
     * @param user
     */
    public void updateUser(User user){
        userDao.save(user);
    }

    /**
     * Method that checks if user exists in database.
     * @param username
     * @return
     */
    public boolean userExists(String username)
    {
        if (userDao.findByUsername(username).size() > 0)
            return true;
        return false;
    }

    /**
     * Method that checks if price is different and update it.
     * @param product ProductInfo object
     * @param newPrice information about new price
     * @return True if price was changed, else False
     */
    public boolean priceChanged(ProductInfo product, double newPrice)
    {
        ProductFlags flags = productFlagsDao.getOne(product.getId());
        double oldPrice = product.getProductBottom();
        if (flags.isPrice_lowers())
        {
            if (oldPrice > newPrice)
            {
                double priceChange = (oldPrice - newPrice)/100 * oldPrice;
                if (priceChange >= flags.getPriceChange())
                    return true;
                else
                    return false;
            }
            else
                return false;
        }
        else
        {
            if (oldPrice <= newPrice)
            {
                double priceChange = (newPrice - oldPrice)/100 * oldPrice;
                if (priceChange >= flags.getPriceChange())
                    return true;
                else
                    return false;
            }
            else
                return false;
        }
    }
}
