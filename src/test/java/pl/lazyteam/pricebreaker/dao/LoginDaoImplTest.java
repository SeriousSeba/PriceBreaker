package pl.lazyteam.pricebreaker.dao;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pl.lazyteam.pricebreaker.entity.User;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class LoginDaoImplTest {

    @Autowired
    LoginDaoImpl loginDao;

    @Autowired
    UserDao userDao;

    User user;

    @Before
    public void setup(){
        user=new User();
        user.setUsername("test");

    }

    @Test
    public void findUser() throws Exception {
        User userFromImpl = loginDao.findUser("test");
    }

    @Test
    public void getUserRoles() throws Exception {
    }

}