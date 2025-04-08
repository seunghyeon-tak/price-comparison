package com.github.seunghyeon_tak.price_comparison.api.service.product;

import com.github.seunghyeon_tak.price_comparison.db.repository.productImage.ProductImagesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImageApiService {
    private final ProductImagesRepository productImagesRepository;

    public List<String> getProductImages(Long productId) {
        return productImagesRepository.findImageUrlByProductId(productId);
    }
}
