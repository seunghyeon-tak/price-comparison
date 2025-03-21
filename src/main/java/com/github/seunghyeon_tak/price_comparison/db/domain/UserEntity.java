package com.github.seunghyeon_tak.price_comparison.db.domain;

import com.github.seunghyeon_tak.price_comparison.db.enums.AlertType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(length = 45)
    private String nickname;

    @Column(length = 45)
    @Enumerated(EnumType.STRING)
    private AlertType alertType;

}
