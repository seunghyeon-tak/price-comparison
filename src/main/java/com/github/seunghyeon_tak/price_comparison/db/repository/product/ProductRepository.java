package com.github.seunghyeon_tak.price_comparison.db.repository.product;

import com.github.seunghyeon_tak.price_comparison.db.domain.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>, ProductRepositoryCustom {
    Page<ProductEntity> findByIsActiveTrue(Pageable pageable);
}
