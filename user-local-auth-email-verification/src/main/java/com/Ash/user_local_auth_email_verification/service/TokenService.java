package com.Ash.user_local_auth_email_verification.service;


import com.Ash.user_local_auth_email_verification.db.TokenRepository;
import com.Ash.user_local_auth_email_verification.entity.Status;
import com.Ash.user_local_auth_email_verification.entity.User;
import com.Ash.user_local_auth_email_verification.entity.UserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TokenService {
    @Autowired
    private EmailService emailService;

    @Autowired
    private TokenRepository tokenRepository;

    public void createToken(User createdUser) {

        String token = UUID.randomUUID().toString();
        UserToken userToken = new UserToken();
        userToken.setUserId(createdUser.getId());
        userToken.setToken(token);
        userToken.setStatus(Status.ACTIVE);

        UserToken createdToken = tokenRepository.save(userToken);

        if (createdToken.getId() != null) {
            System.out.println("Sending email to the user ");
            emailService.sendEmail(token, createdUser.getEmail());
        }



    }
}
