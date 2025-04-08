package com.github.seunghyeon_tak.price_comparison.api.controller.product;

import com.github.seunghyeon_tak.price_comparison.api.business.product.ProductApiBusiness;
import com.github.seunghyeon_tak.price_comparison.common.annotation.ControllerLoggable;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.product.ProductDetailDto;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.product.ProductsDto;
import com.github.seunghyeon_tak.price_comparison.common.exception.response.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductApiController {
    private final ProductApiBusiness productApiBusiness;

    @GetMapping
    @ControllerLoggable("상품 목록 컨트롤러")
    public Api<Page<ProductsDto>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,DESC") String sort
    ) {
        String[] sortParams = sort.split(",");
        PageRequest pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0])
        );
        Page<ProductsDto> response = productApiBusiness.list(pageable);
        return Api.success(response);
    }

    @GetMapping("/{productId}")
    @ControllerLoggable("상품 상세 컨트롤러")
    public Api<ProductDetailDto> detailProduct(@PathVariable Long productId) {
        ProductDetailDto response = productApiBusiness.detail(productId);

        return Api.success(response);
    }
}
