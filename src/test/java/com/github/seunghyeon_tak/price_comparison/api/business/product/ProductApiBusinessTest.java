package com.github.seunghyeon_tak.price_comparison.api.business.product;

import com.github.seunghyeon_tak.price_comparison.api.converter.product.ProductApiConverter;
import com.github.seunghyeon_tak.price_comparison.api.service.product.ProductApiService;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.product.ProductsDto;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductApiBusinessTest {
    @Mock
    private ProductApiService productApiService;

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
}