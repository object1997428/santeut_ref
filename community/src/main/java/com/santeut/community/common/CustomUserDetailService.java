package com.santeut.community.common;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailService implements UserDetailsService {

    @Override
    public MyUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return null;
    }
}
