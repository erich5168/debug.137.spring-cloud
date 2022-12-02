package com.appsdeveloperblog.photo.ApiUser.security;

import com.appsdeveloperblog.photo.ApiUser.ui.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.Filter;

/**
 * 94. Created
 */
@Configuration
@EnableWebSecurity
public class AppWebSecurity extends WebSecurityConfigurerAdapter {

    private UsersService usersService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${gateway.ip}")
    private String gatewayIp;
    @Value("${token.secret}")
    private String tokenSecret;
    @Value("${token.expiration_time}")
    private String tokenTime;
    @Value("${login.url.path}")
    private String loginPath;

    @Autowired
    public AppWebSecurity(UsersService usersService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.usersService = usersService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/users/**").hasIpAddress(gatewayIp) // 90. Only allow request come from Api-Gateway
                //.antMatchers("/users/**").permitAll()
                .and()
                .addFilter(getAuthenticationFilter()); // 94. Register AuthenticationFilter to WebSecurity

        // 89. [3:19] H2-console runs within a frame
        http.headers().frameOptions().disable();
    }

    // 94. Register AuthenticationFilter to WebSecurity
    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        // 1. Create instance
        AuthenticationFilter authFilter = new AuthenticationFilter(usersService, authenticationManager(), tokenSecret, tokenTime);
        // 2. Set authenticationManger() to AuthenticationFilter because see attemptAuthentication() where it calls getAuthenicationManger()
        // authFilter.setAuthenticationManager(authenticationManager());

        // 98. Customize Login URL
        authFilter.setFilterProcessesUrl(loginPath);

        return authFilter;
    }

    // Ignore HttpSecurity Patten on this particular path
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/users/status/ok");
//    }

    /**
     * 95. Configure AuthenticationManager() let Spring Security know which service.class to use to load userDetails & which passwordEncoder()
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usersService).passwordEncoder(bCryptPasswordEncoder);
    }

}
