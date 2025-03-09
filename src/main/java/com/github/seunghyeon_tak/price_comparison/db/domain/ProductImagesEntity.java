package com.github.seunghyeon_tak.price_comparison.db.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_images")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductImagesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(nullable = false)
    private String url;

    @Column(columnDefinition = "TINYINT(0) DEFAULT 0")
    private Boolean isMain;
}
