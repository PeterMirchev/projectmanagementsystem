package com.fixer.service.impl;

import com.fixer.config.JwtProvider;
import com.fixer.model.User;
import com.fixer.repository.UserRepository;
import com.fixer.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findUserProfileByJwt(String jwt) throws Exception {

        String email = JwtProvider.getEmailFromToken(jwt);

        return findUserByEmail(email);
    }

    @Override
    public User findUserByEmail(String email) throws Exception {

        User user = userRepository.findByEmail(email);

        if(user == null) {
            throw new Exception(String.format("user with {%s} not found", email));
        }

        return user;
    }

    @Override
    public User findUserById(Long userId) throws Exception {

        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new Exception(String.format("User with ID {%s} not found.", userId));
        }

        return user.get();
    }

    @Override
    public User updateUsersProjectSize(User user, int number) throws Exception {

        user.setProjectSize(user.getProjectSize() + number);

        return userRepository.save(user);
    }
}
