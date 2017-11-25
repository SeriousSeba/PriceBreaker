package pl.lazyteam.pricebreaker.service;

import org.springframework.security.core.userdetails.UserDetailsService;

//TODO


//@Service
public class LoginServiceImpl// implements UserDetailsService
{
/*    LoginDao logindao;

    @Autowired
    public void setLogindao(LoginDao logindao)
    {
        this.logindao = logindao;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user = logindao.findClient(username);

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

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantList);

        return userDetails;
    }*/
}
