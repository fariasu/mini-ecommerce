package com.farias.mini_ecommerce.modules.user.repository;

import com.farias.mini_ecommerce.modules.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("SELECT u FROM tb_users u WHERE u.email = :userEmail")
    Optional<User> findByEmail(@Param("userEmail")String email);

    @Query("SELECT COUNT(u) > 0 FROM tb_users u WHERE u.email = :userEmail")
    Boolean existsByEmail(@Param("userEmail") String email);
}
