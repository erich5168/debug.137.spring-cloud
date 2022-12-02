package com.appsdeveloperblog.photo.ApiUser.ui.service;

import com.appsdeveloperblog.photo.ApiUser.ui.model.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 *
 * Footnotes:
 * 95. UserDetailsService is a Spring security interface.  Used in WebSecurity
 * In order for our UsersService interface be an instance of UserDetailsService we must extends it.
 */
public interface UsersService extends UserDetailsService {
    UserDto createUser(UserDto userDetail);

    // 96. start 2:35 added a new method to retrieve user
    UserDto getUserDetailsByEmail(String emailAsUsername);
}
