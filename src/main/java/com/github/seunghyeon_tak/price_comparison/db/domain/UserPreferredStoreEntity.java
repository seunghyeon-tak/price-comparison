package com.github.seunghyeon_tak.price_comparison.db.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_preferred_store")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserPreferredStoreEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private StoreEntity store;
}
