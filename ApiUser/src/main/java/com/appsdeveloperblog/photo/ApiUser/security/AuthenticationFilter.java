package com.appsdeveloperblog.photo.ApiUser.security;


import com.appsdeveloperblog.photo.ApiUser.ui.model.LoginRequestModel;
import com.appsdeveloperblog.photo.ApiUser.ui.model.UserDto;
import com.appsdeveloperblog.photo.ApiUser.ui.service.UsersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 94. Register this class with AppWebSecurity.  This class will get triggered everytime when user is trying to login.
 */
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

    private UsersService usersService;

    // @Value did work here because this class is not Spring Managed Bean....
    // https://stackoverflow.com/questions/74226453/does-value-field-injection-require-to-be-under-a-restcontroller-configurati?noredirect=1#comment131048957_74226453
//    @Value("${token.secret}")
    private String tokenSecret;
//    @Value("${token.expiration_time}")
    private String tokenTime;


    @Autowired
    public AuthenticationFilter(UsersService usersService, AuthenticationManager authenticationManager, String tokenSecret, String tokenTime) {
        super(authenticationManager);
        this.usersService = usersService;
        this.tokenSecret = tokenSecret;
        this.tokenTime = tokenTime;
    }


    /**
     * This method will be called by Spring Security Framework during login.
     * There is method that we need to override to help us extract username & password.
     * And to call authenticate method on the Authentication Manager
     *
     * @param request - Use to read username & password
     * @param response
     * @return Authentication
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        try {
            // ObjectMapper() be used to map username & password to LoginRequestModel
            LoginRequestModel cred = new ObjectMapper().readValue(request.getInputStream(), LoginRequestModel.class);

            /*
              !! Notice: Using email as Username
              Sequence as follows:
              1. Username & password pass in to UsernamePasswordAuthenticationToken()
              2. Pass to authenticat() object -> than to AuthenticationManager()
             */
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            cred.getEmail(),
                            cred.getPassword(),
                            new ArrayList<>()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Footnotes:
     * 94. Spring security will call this method upon success login.
     * 96. Job is to take user detail wrap it within JWT token.  Include it in the response Header.
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        /* TODO:  Implementation
           	1. Take userDetails.userId -> generate JWT token.
            2. Add JWT token to responseHeader.
            3. Client be able to read JWT token from responseHeader
                Use JWT in subsequent request to Authorization Header.
         */
        // 1. Get basic info about the login user
        String emailAsUsername = ((User)auth.getPrincipal()).getUsername();  // Get email from the user that already login
        UserDto userDto = usersService.getUserDetailsByEmail(emailAsUsername); // use the same email to retrieve details such as ID..etc.

        // 137. Cloud Bus - broad cast change
        log.info("TokenSecrete: {}", tokenSecret);
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("tokenSecret", tokenSecret);
        claims.put("sub", userDto.getUserId());

        // 2. JWT Webtoken

        // Original from class
//        String token = Jwts.builder()
//                .setSubject(userDto.getUserId())
//                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(tokenTime)))
//                .signWith(SignatureAlgorithm.HS512, tokenSecret)
//                .compact();

        // Modify added token secrete to claim for trouble shooting
        String token = Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(tokenTime)))
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();

        // 3. Add to response Header
        res.addHeader("token", token);
        res.addHeader("userId", userDto.getUserId());
    }
}
