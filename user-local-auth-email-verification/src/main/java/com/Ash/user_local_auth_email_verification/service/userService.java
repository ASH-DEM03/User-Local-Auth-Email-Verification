package com.Ash.user_local_auth_email_verification.service;

import com.Ash.user_local_auth_email_verification.db.UserRepository;
import com.Ash.user_local_auth_email_verification.dto.UserDTO;
import com.Ash.user_local_auth_email_verification.entity.Status;
import com.Ash.user_local_auth_email_verification.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class userService {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public void register(UserDTO userDTO) {
        User u = new User();
        u.setPassword(passwordEncoder.encode(userDTO.password()));
        u.setUsername(userDTO.username());
        u.setEmail(userDTO.email());
        u.setStatus(Status.INACTIVE);

        User createdUser = userRepository.save(u);

        if(createdUser.getId() != null) {
            tokenService.createToken(createdUser);
        }

    }
}