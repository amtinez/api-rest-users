package com.amtinez.api.rest.users.security.impl;

import com.amtinez.api.rest.users.daos.UserDao;
import com.amtinez.api.rest.users.mappers.UserDetailsMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserDetailsMapper userDetailsMapper;

    @Resource
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(final String email) {
        return userDao.findByEmail(email)
                      .map(userDetailsMapper::userModelToUserDetails)
                      .orElseThrow(() -> new UsernameNotFoundException(String.format("User with the email: %s not found", email)));
    }

}
