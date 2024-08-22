package com.fixer.service.impl;

import com.fixer.model.User;
import com.fixer.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username);

        if (user == null) {
            throw  new UsernameNotFoundException(String.format("User with email {%s} does not exist.",username));
        }

        List<GrantedAuthority> authorities = new ArrayList<>();

        return new org
                .springframework
                .security
                .core
                .userdetails
                .User(user.getEmail(), user.getPassword(), authorities);
    }
}
