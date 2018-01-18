package pl.lazyteam.pricebreaker.service;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pl.lazyteam.pricebreaker.entity.User;
import pl.lazyteam.pricebreaker.entity.UserRole;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserServiceImplTest {

    @Autowired
    UserServiceImpl userService;

    private static boolean setUpIsDone = false;

    @Before
    public void setUp() {
        if (!setUpIsDone) {
            userService.add("test", "1234","test@test.com");
            userService.add("test1", "1234","test@test.com");
            userService.add("test2", "1234","test@test.com");
        }
        setUpIsDone = true;
    }
    @BeforeClass
    public static void setup(){

    }
    @Test
    public void list() throws Exception {
        List<User> userList = userService.list();
        assertEquals(userList.size(),4);
    }

    @Test
    public void findUserByUsername() throws Exception {
        User user = userService.findUserByUsername("test");
        assertEquals(user.getUsername(), "test");
    }

    @Test
    public void validatePassword() throws Exception {
        User user = userService.findUserByUsername("test");
        //assertEquals(userService.validatePassword("1234"));
    }

    @Test
    public void delete() throws Exception {
        userService.add("test3", "1234","test@test.com");
        userService.delete("test3");
        assertEquals(userService.list().size(), 3);
    }

    @Test
    public void emailExists() throws Exception {
        assertEquals(userService.emailExists("test@test.com"), true);
        assertEquals(userService.emailExists(null), false);
    }

    @Test
    public void update() throws Exception {
    }

    @Test
    public void add() throws Exception {
        userService.add("test4", "1234","test@test.com");
        assertEquals(userService.list().size(), 5);
        userService.delete("test4");

    }

    @Test
    public void updateUser() throws Exception {
        userService.updateUser(new User("test", "test", "test@test.com", new UserRole("test", "ROLE_ADMIN")));
    }

    @Test
    public void userExists() throws Exception {
        assertEquals(userService.userExists("test"), true);
        assertEquals(userService.userExists("tesasdft"), false);
    }

}