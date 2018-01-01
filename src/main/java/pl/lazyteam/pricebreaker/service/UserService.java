package pl.lazyteam.pricebreaker.service;

import pl.lazyteam.pricebreaker.entity.User;
import pl.lazyteam.pricebreaker.entity.VerificationToken;

import java.util.List;

public interface UserService
{
    public List<User> list();

    public User findUserByUsername(String username);

    public void update(String username, String password);

    public boolean validatePassword(String password);

    public void add(String username, String password, String email);

    public void delete(String username);

    public boolean userExists(String username);

    public boolean emailExists(String email);

    public VerificationToken getVerificationToken(String verificationToken);

    public void createVerificationTokenForUser(User user, String token);

    public String validateVerificationToken(String token);
}
