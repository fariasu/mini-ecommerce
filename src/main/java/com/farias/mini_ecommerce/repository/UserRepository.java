package com.farias.mini_ecommerce.repository;

import com.farias.mini_ecommerce.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
