package pl.lazyteam.pricebreaker.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.lazyteam.pricebreaker.entity.User;
import pl.lazyteam.pricebreaker.service.UserServiceImpl;

import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class AdminControllerTest extends TestCase {


    private MockMvc mvc;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private AdminController adminController;

    @Before
    public void setup() {
        // We would need this line if we would not use MockitoJUnitRunner
        // MockitoAnnotations.initMocks(this);
        // Initializes the JacksonTester
        JacksonTester.initFields(this, new ObjectMapper());
        // MockMvc standalone approach
        mvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }
    @Test
    public void testShowUsers() throws Exception {
        given(userService.findUserByUsername("test")).willReturn(null);
    }

    public void testDeleteUser() throws Exception {
    }

}