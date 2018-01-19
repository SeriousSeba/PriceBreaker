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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class LoginDaoImplTest {

    @Autowired
    LoginDaoImpl loginDao;

    @Autowired
    UserDao userDao;

    private static boolean setUpIsDone = false;

    @Before
    public void setUp() {
        if (!setUpIsDone) {
            userDao.save(new User("test1", "1234","test1@test.com",new UserRole("test", "USER_ROLE")));
            userDao.save(new User("test2", "1234","test2@test.com",new UserRole("test", "USER_ROLE")));
            userDao.save(new User("test3", "1234","test3@test.com",new UserRole("test", "USER_ROLE")));
        }
        setUpIsDone = true;
    }

    @Test
    public void findUser() throws Exception {
        assertEquals("test1", loginDao.findUser("test1").getUsername());
    }

    @Test
    public void getUserRoles() throws Exception {
        assertEquals("USER_ROLE", loginDao.getUserRoles("test1").get(0));
    }

    @Test
    public void contextLoads() throws Exception {
        assertThat(loginDao).isNotNull();
    }

}