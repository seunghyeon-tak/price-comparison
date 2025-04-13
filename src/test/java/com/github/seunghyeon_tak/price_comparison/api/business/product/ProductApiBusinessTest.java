package com.github.seunghyeon_tak.price_comparison.api.business.product;

import com.github.seunghyeon_tak.price_comparison.api.converter.product.ProductApiConverter;
import com.github.seunghyeon_tak.price_comparison.api.service.product.ProductApiService;
import com.github.seunghyeon_tak.price_comparison.api.service.product.ProductImageApiService;
import com.github.seunghyeon_tak.price_comparison.api.service.product.ProductPriceApiService;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.product.ProductDetailDto;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.product.ProductPriceDto;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.product.ProductsDto;
import com.github.seunghyeon_tak.price_comparison.common.exception.ApiException;
import com.github.seunghyeon_tak.price_comparison.common.exception.response.enums.product.ProductResponseCode;
import com.github.seunghyeon_tak.price_comparison.db.domain.ProductEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductApiBusinessTest {
    @Mock
    private ProductApiService productApiService;

    @Mock
    private ProductImageApiService productImageApiService;

    @Mock
    private ProductPriceApiService productPriceApiService;

    @Mock
    private ProductApiConverter productApiConverter;

    @InjectMocks
    private ProductApiBusiness productApiBusiness;

    private ProductEntity productEntity() {
        return ProductEntity.builder()
                .id(1L)
                .name("상품1")
                .description("설명")
                .purchaseUrl("url")
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private ProductsDto productsDto() {
        return ProductsDto.builder()
                .id(1L)
                .name("상품1")
                .description("설명")
                .purchaseUrl("url")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("상품_목록_반환_성공")
    void test1() {
        // given
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

        PageImpl<ProductEntity> entityPage = new PageImpl<>(List.of(productEntity()));
        PageImpl<ProductsDto> dtoPage = new PageImpl<>(List.of(productsDto()));

        when(productApiService.getActiveProducts(pageable)).thenReturn(entityPage);
        when(productApiConverter.toResponsePage(entityPage)).thenReturn(dtoPage);

        // when
        Page<ProductsDto> result = productApiBusiness.list(pageable);

        // then
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("상품1");
    }

    @Test
    @DisplayName("상품_목록이_비어_있는_경우")
    void test2() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductEntity> emptyEntityPage = new PageImpl<>(List.of());
        Page<ProductsDto> emptyDtoPage = new PageImpl<>(List.of());

        when(productApiService.getActiveProducts(pageable)).thenReturn(emptyEntityPage);
        when(productApiConverter.toResponsePage(emptyEntityPage)).thenReturn(emptyDtoPage);

        // when
        Page<ProductsDto> result = productApiBusiness.list(pageable);

        // then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("상품_상세_조회_성공")
    void test3() {
        // given
        Long productId = 1L;
        ProductEntity productEntity = ProductEntity.builder()
                .id(productId)
                .name("테스트 상품")
                .description("상세 설명")
                .purchaseUrl("https://test.com")
                .isActive(true)
                .createdAt(LocalDateTime.of(2025, 4, 12, 10, 30))
                .build();

        String categoryName = "전자제품";
        List<String> imagesUrls = List.of("http://image1.jpg", "http://image2.jpg");
        int lowestPrice = 10000;

        ProductPriceDto store1 = ProductPriceDto.builder()
                .storeName("11번가")
                .price(20000)
                .productUrl("http://test1.com")
                .lastUpdated(LocalDateTime.of(2025, 4, 13, 0, 0))
                .build();
        ProductPriceDto store2 = ProductPriceDto.builder()
                .storeName("g마켓")
                .price(10000)
                .productUrl("http://test2.com")
                .lastUpdated(LocalDateTime.of(2025, 4, 12, 0, 0))
                .build();
        List<ProductPriceDto> productPriceDtoList = List.of(store1, store2);

        ProductDetailDto productDetailDto = ProductDetailDto.builder()
                .id(productEntity.getId())
                .name(productEntity.getName())
                .category(categoryName)
                .lowestPrice(lowestPrice)
                .images(imagesUrls)
                .description(productEntity.getDescription())
                .stores(productPriceDtoList)
                .createdAt(productEntity.getCreatedAt())
                .build();

        when(productApiService.getProduct(productId)).thenReturn(productEntity);
        when(productApiService.getProductCategoryName(productId)).thenReturn(categoryName);
        when(productImageApiService.getProductImages(productId)).thenReturn(imagesUrls);
        when(productPriceApiService.getProductPriceDto(productId)).thenReturn(productPriceDtoList);
        when(productApiConverter.toDetailResponse(productEntity, categoryName, imagesUrls, productPriceDtoList, lowestPrice)).thenReturn(productDetailDto);

        // when
        ProductDetailDto result = productApiBusiness.detail(productId);

        // then
        assertThat(result).isEqualTo(productDetailDto);
        verify(productApiService).getProduct(productId);
        verify(productApiService).getProductCategoryName(productId);
        verify(productImageApiService).getProductImages(productId);
        verify(productPriceApiService).getProductPriceDto(productId);
        verify(productApiConverter).toDetailResponse(productEntity, categoryName, imagesUrls, productPriceDtoList, lowestPrice);
    }

    @Test
    @DisplayName("존재하지_않는_상품")
    void test4() {
        // given
        Long productId = 999L;

        when(productApiService.getProduct(productId))
                .thenThrow(new ApiException(ProductResponseCode.PRODUCT_NOT_FOUND));

        // when & then
        ApiException exception = assertThrows(ApiException.class, () -> {
            productApiBusiness.detail(productId);
        });

        assertThat(exception.getErrorDescription())
                .isEqualTo(ProductResponseCode.PRODUCT_NOT_FOUND.getDescription());

        verify(productApiService).getProduct(productId);
    }

    @Test
    @DisplayName("삭제된_상품")
    void test5() {
        // given
        Long productId = 100L;

        when(productApiService.getProduct(productId))
                .thenThrow(new ApiException(ProductResponseCode.PRODUCT_DELETED));

        // when & then
        ApiException exception = assertThrows(ApiException.class, () -> {
            productApiBusiness.detail(productId);
        });

        assertThat(exception.getErrorDescription())
                .isEqualTo(ProductResponseCode.PRODUCT_DELETED.getDescription());

        verify(productApiService).getProduct(productId);
    }

    @Test
    @DisplayName("판매_종료된_상품")
    void test6() {
        // given
        Long productId = 101L;

        when(productApiService.getProduct(productId))
                .thenThrow(new ApiException(ProductResponseCode.PRODUCT_SOLD_OUT));

        // when & then
        ApiException exception = assertThrows(ApiException.class, () -> {
            productApiBusiness.detail(productId);
        });

        assertThat(exception.getErrorDescription())
                .isEqualTo(ProductResponseCode.PRODUCT_SOLD_OUT.getDescription());

        verify(productApiService).getProduct(productId);
    }

    @Test
    @DisplayName("상품_상세_입력_파라미터_오류")
    void test7() {
        // given
        Long invalidProductId = -1L;

        when(productApiService.getProduct(invalidProductId))
                .thenThrow(new ApiException(ProductResponseCode.INVALID_PRODUCT_ID));

        // when & then
        ApiException exception = assertThrows(ApiException.class, () -> {
            productApiBusiness.detail(invalidProductId);
        });

        assertThat(exception.getErrorDescription())
                .isEqualTo(ProductResponseCode.INVALID_PRODUCT_ID.getDescription());

        verify(productApiService).getProduct(invalidProductId);
    }
}
