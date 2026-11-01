package com.Ash.user_local_auth_email_verification.service;

import com.Ash.user_local_auth_email_verification.db.TokenRepository;
import com.Ash.user_local_auth_email_verification.db.UserRepository;
import com.Ash.user_local_auth_email_verification.entity.Status;
import com.Ash.user_local_auth_email_verification.entity.UserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenVerificationService {
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private UserRepository userRepository;
    public boolean verify(String token){
        UserToken userToken = tokenRepository.findByToken(token);
        if (userToken != null){
            userToken.setStatus(Status.INACTIVE);
            tokenRepository.save(userToken);

            userRepository.findById(userToken.getUserId()).ifPresent( user ->{
                user.setStatus(Status.ACTIVE);
                userRepository.save(user);

            });
            return true;
        }

        return false;
    }
}
