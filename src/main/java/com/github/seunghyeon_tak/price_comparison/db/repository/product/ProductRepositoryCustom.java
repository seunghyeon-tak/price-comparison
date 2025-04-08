package com.github.seunghyeon_tak.price_comparison.db.repository.product;

import java.util.Optional;

public interface ProductRepositoryCustom {
    // select c.name from products p
    // join categories c on p.category_id = c.id
    // where p.id = :productId
    String findCategoryNameByProductId(Long productId);
}
