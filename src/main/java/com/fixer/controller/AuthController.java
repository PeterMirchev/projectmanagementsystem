package com.fixer.controller;

import com.fixer.config.JwtProvider;
import com.fixer.model.User;
import com.fixer.repository.UserRepository;
import com.fixer.request.LoginRequest;
import com.fixer.response.AuthResponse;
import com.fixer.service.SubscriptionService;
import com.fixer.service.impl.CustomUserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.fixer.config.BaseMessagesAndPaths.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsImpl customUserDetails;
    private final SubscriptionService subscriptionService;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          CustomUserDetailsImpl customUserDetails,
                          SubscriptionService subscriptionService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.customUserDetails = customUserDetails;
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws Exception {

        User isUserExist = userRepository.findByEmail(user.getEmail());

        if (isUserExist != null) {
            throw new Exception(String.format(USER_ALREADY_EXIST, user.getEmail()));
        }

        User createUser = generateUser(user);

        User persistedUser = userRepository.save(createUser);

        subscriptionService.createSubscription(persistedUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = JwtProvider.generatedToken(authentication);

        AuthResponse res = new AuthResponse();
        res.setMessage(SIGNUP_SUCCESS);
        res.setJwt(jwt);

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }


    @PostMapping("signing")
    public ResponseEntity<AuthResponse> signing(@RequestBody LoginRequest loginRequest) {

        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = JwtProvider.generatedToken(authentication);

        AuthResponse res = new AuthResponse();
        res.setMessage(SIGNING_SUCCESS);
        res.setJwt(jwt);

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    private Authentication authenticate(String username, String password) {

        UserDetails userDetails = customUserDetails.loadUserByUsername(username);

        if (userDetails == null) {
            throw new BadCredentialsException(INVALID_USERNAME);
        }
        if(!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException(INVALID_PASSWORD);
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private User generateUser(User user) {

        User createUser = new User();
        createUser.setPassword(passwordEncoder.encode(user.getPassword()));
        createUser.setEmail(user.getEmail());
        createUser.setFullName(user.getFullName());
        return createUser;
    }
}
