package com.Ash.user_local_auth_email_verification.db;

import com.Ash.user_local_auth_email_verification.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
