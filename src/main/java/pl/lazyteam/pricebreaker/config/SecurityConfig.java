package pl.lazyteam.pricebreaker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import pl.lazyteam.pricebreaker.service.LoginServiceImpl;

//TODO


@Repository
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{

/*    @Autowired
    LoginServiceImpl loginServiceImpl;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(loginServiceImpl);
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider()
    {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(loginServiceImpl);
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }*/

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    protected void configure(HttpSecurity http) throws Exception
    {
        http.csrf().disable();

        http.authorizeRequests().antMatchers("/register","/registerSuccess").permitAll();
        http.authorizeRequests().antMatchers("/home").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')");

    }
}