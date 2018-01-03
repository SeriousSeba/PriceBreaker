package pl.lazyteam.pricebreaker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.lazyteam.pricebreaker.entity.User;
import pl.lazyteam.pricebreaker.dao.LoginDao;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoginServiceImpl implements UserDetailsService
{

    LoginDao logindao;

    @Autowired
    public void setLoginDao(LoginDao logindao)
    {
        this.logindao = logindao;
    }
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user = logindao.findUser(username);
        if(user == null)
        {
            throw new UsernameNotFoundException("User doesn't exist in database");
        }
        List<String> roles = logindao.getUserRoles(username);
        List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
        if(roles != null)
        {
            for(String role : roles)
            {
                GrantedAuthority auth = new SimpleGrantedAuthority(role);
                grantList.add(auth);
            }
        }
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.isEnabled(), true, true, true, grantList);
        return userDetails;
    }
}
