package com.github.seunghyeon_tak.price_comparison.api.controller.price_alert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.seunghyeon_tak.price_comparison.api.business.price_alert.PriceAlertsApiBusiness;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.request.price_alert.PriceAlertsRequest;
import com.github.seunghyeon_tak.price_comparison.common.exception.ApiException;
import com.github.seunghyeon_tak.price_comparison.common.exception.response.enums.price_alert.PriceAlertResponseCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PriceAlertsApiController.class)
@AutoConfigureMockMvc(addFilters = false)
class PriceAlertsApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PriceAlertsApiBusiness priceAlertsApiBusiness;

    private void userSecurityContext(Long userId) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken(String.valueOf(userId), null);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @Test
    @DisplayName("가격 알림 등록 성공")
    void test1() throws Exception {
        // given
        Long userId = 1L;
        Long productId = 2L;
        userSecurityContext(userId);
        PriceAlertsRequest request = PriceAlertsRequest.builder()
                .productId(productId)
                .targetPrice(BigDecimal.valueOf(1000))
                .build();

        doNothing().when(priceAlertsApiBusiness).registerPriceAlert(userId, request);

        // when & then
        mockMvc.perform(post("/api/v1/price-alerts")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.resultCode").value(200))
                .andExpect(jsonPath("$.result.resultMessage").value("success"))
                .andExpect(jsonPath("$.result.resultDescription").value("success"))
                .andDo(print());
    }

    @Test
    @DisplayName("같은 가격으로 동일한 상품이 알림 등록되어있을때")
    void test2() throws Exception {
        // given
        Long userId = 1L;
        Long productId = 2L;
        userSecurityContext(userId);

        PriceAlertsRequest request = PriceAlertsRequest.builder()
                .productId(productId)
                .targetPrice(BigDecimal.valueOf(1000))
                .build();

        doThrow(new ApiException(PriceAlertResponseCode.DUPLICATE_PRODUCT))
                .when(priceAlertsApiBusiness).registerPriceAlert(userId, request);

        // when & then
        mockMvc.perform(post("/api/v1/price-alerts")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result.resultCode").value(6000))
                .andExpect(jsonPath("$.result.resultMessage").value("이미 같은 가격에 등록된 알림이 있습니다."))
                .andDo(print());

        verify(priceAlertsApiBusiness).registerPriceAlert(userId, request);
    }

}