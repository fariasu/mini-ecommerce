package com.farias.mini_ecommerce.modules.user.entity;

import com.farias.mini_ecommerce.modules.user.entity.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity(name = "tb_users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "The name must not be null.")
    @Size(max = 128)
    private String name;

    @Email(message = "Invalid email format.")
    @NotNull(message = "The email must not be null.")
    private String email;

    @NotNull(message = "The password must not be null.")
    @Size(max = 256)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
            message = "The password must contain at least one uppercase letter, one lowercase letter, one number, and one special character.")
    private String password;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private UserRole userRole = UserRole.ROLE_USER;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
