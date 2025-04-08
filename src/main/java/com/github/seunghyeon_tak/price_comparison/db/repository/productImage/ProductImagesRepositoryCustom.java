package com.github.seunghyeon_tak.price_comparison.db.repository.productImage;

import java.util.List;

public interface ProductImagesRepositoryCustom {
    List<String> findImageUrlByProductId(Long productId);
}
