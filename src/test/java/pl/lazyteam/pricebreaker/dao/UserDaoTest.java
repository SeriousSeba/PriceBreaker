package pl.lazyteam.pricebreaker.dao;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import pl.lazyteam.pricebreaker.entity.User;
import pl.lazyteam.pricebreaker.entity.UserRole;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserDaoTest {

    @Autowired
    private UserDao userDao;
    private static boolean setUpIsDone = false;
    User user;

    @Before
    public void setUp() {
        if (!setUpIsDone) {
            user = new User("test", "test", "test@test.com", new UserRole("test", "ROLE_USER"));
            userDao.save(user);
            setUpIsDone = true;
        }

    }
    @Test
    public void findAll() throws Exception {
        List<User> users=userDao.findAll();
        assertEquals(4,users.size());
    }

    @Test
    public void findByUsername() throws Exception {
        User user = userDao.findByUsername("test").get(0);
        assertEquals("test", user.getUsername());
    }

    @Test
    public void findByEmail() throws Exception {
        User user = userDao.findByEmail("test@test.com").get(0);
        assertEquals("test@test.com", user.getEmail());
    }

    @Test
    public void deleteByUsername() throws Exception {
        userDao.deleteByUsername("test");
        userDao.save(new User("test", "test", "test@test.com", new UserRole("test", "ROLE_USER")));
    }

}