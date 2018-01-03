package pl.lazyteam.pricebreaker.entity;

import junit.framework.Assert;
import junit.framework.TestCase;

public class UserTest extends TestCase {
    String userName="user1";
    String pass="user1";
    String email="user1@gmail.com";
    UserRole userRole=new UserRole(userName,"ROLE_USER");
    User user=new User(userName, pass,email, userRole);
    public void testGetUserRole() throws Exception {
        Assert.assertEquals(new UserRole(userName,"ROLE_USER"), user.getUserRole());
    }

    public void testSetUserRole() throws Exception {
        user.setUserRole(new UserRole());
        Assert.assertEquals(new UserRole(), user.getUserRole());
    }

    public void testGetUser_id() throws Exception {
        Assert.assertEquals(0, user.getUser_id());
    }

    public void testSetUser_id() throws Exception {
        user.setUser_id(1);
        Assert.assertEquals(1, user.getUser_id());
    }

    public void testGetUsername() throws Exception {
        Assert.assertEquals(userName,user.getUsername());
    }

    public void testSetUsername() throws Exception {
        String username="user2";
        user.setUsername(username);
        Assert.assertEquals(username,user.getUsername());
    }

    public void testGetPassword() throws Exception {
        assertEquals(pass,user.getPassword());
    }

    public void testSetPassword() throws Exception {
        String pass="123";
        user.setPassword(pass);
        assertEquals(pass,user.getPassword());
    }

    public void testGetEmail() throws Exception {
        assertEquals(email,user.getEmail());
    }

    public void testSetEmail() throws Exception {
        String email="aaaa@adf.com";
        user.setEmail(email);
        assertEquals(email,user.getEmail());
    }

    public void testIsEnabled() throws Exception {
        assertEquals(false,user.isEnabled());
    }

    public void testSetEnabled() throws Exception {
        user.setEnabled(true);
        assertEquals(true, user.isEnabled());
    }


}