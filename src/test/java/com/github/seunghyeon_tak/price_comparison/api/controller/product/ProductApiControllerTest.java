package com.github.seunghyeon_tak.price_comparison.api.controller.product;

import com.github.seunghyeon_tak.price_comparison.api.business.product.ProductApiBusiness;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.product.ProductsDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductApiController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductApiBusiness productApiBusiness;

    private ProductsDto sampleProduct() {
        return ProductsDto.builder()
                .id(1L)
                .name("테스트 상품")
                .description("설명")
                .purchaseUrl("https://example.com")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("상품_목록_조회_성공")
    void test1() throws Exception {
        // given
        Page<ProductsDto> mockPage = new PageImpl<>(List.of(sampleProduct()));
        when(productApiBusiness.list(any())).thenReturn(mockPage);

        // when & then
        mockMvc.perform(get("/api/v1/products")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "createdAt,DESC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.resultCode").value(200))
                .andExpect(jsonPath("$.result.resultMessage").value("success"))
                .andExpect(jsonPath("$.result.resultDescription").value("success"))
                .andExpect(jsonPath("$.data.content[0].name").value("테스트 상품"))
                .andDo(print());
    }

    @Test
    @DisplayName("상품_목록_조회 - 데이터 없음")
    void test2() throws Exception {
        // given
        Page<ProductsDto> emptyPage = new PageImpl<>(List.of());
        when(productApiBusiness.list(any())).thenReturn(emptyPage);

        // when & then
        mockMvc.perform(get("/api/v1/products")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "createdAt,DESC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content").isEmpty());
    }

    @Test
    @DisplayName("상품_목록_조회 - 잘못된 정렬 값")
    void test3() throws Exception {
        mockMvc.perform(get("/api/v1/products")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "createdAt,WRONG"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("상품_목록_조회 - 음수 페이지")
    void test4() throws Exception {
        mockMvc.perform(get("/api/v1/products")
                        .param("page", "-1")
                        .param("size", "10")
                        .param("sort", "createdAt,DESC"))
                .andExpect(status().isBadRequest());
    }
}