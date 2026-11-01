package com.Ash.user_local_auth_email_verification.db;


import com.Ash.user_local_auth_email_verification.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<UserToken, Long>{
    UserToken findByToken(String token);
}
