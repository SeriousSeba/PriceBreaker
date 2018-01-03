package pl.lazyteam.pricebreaker.config;

import junit.framework.TestCase;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.lazyteam.pricebreaker.service.LoginServiceImpl;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;


public class SecurityConfigTest extends TestCase {
    SecurityConfig securityConfig = new SecurityConfig();


    public void instanceClass() throws Exception{
        assertThat(securityConfig, instanceOf(SecurityConfig.class));
    }

    public void testUserDetailsServiceBean() throws Exception {
        assertThat(securityConfig.userDetailsServiceBean(), instanceOf(LoginServiceImpl.class));
    }


    public void testAuthenticationProvider() throws Exception {
        assertThat(securityConfig.authenticationProvider(), instanceOf(DaoAuthenticationProvider.class));

    }

    public void testPasswordEncoder() throws Exception {
        assertThat(securityConfig.passwordEncoder(), instanceOf(BCryptPasswordEncoder.class));

    }

}