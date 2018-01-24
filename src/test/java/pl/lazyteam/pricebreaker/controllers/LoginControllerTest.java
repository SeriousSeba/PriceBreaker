package pl.lazyteam.pricebreaker.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class LoginControllerTest {

    @Autowired
    LoginController loginController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void login() throws Exception {
        loginController = mock(LoginController.class);
        this.mockMvc.perform(get("/login")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("user/login/login"));
    }

    @Test
    public void logoutPage() throws Exception {
        loginController = mock(LoginController.class);
        this.mockMvc.perform(get("/logout")).andDo(print()).andExpect(status().is3xxRedirection());
    }

    @Test
    public void home() throws Exception {
        loginController = mock(LoginController.class);
        this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().is3xxRedirection());
    }

    @Test
    public void contextLoads() throws Exception {
        assertThat(loginController).isNotNull();
    }

}