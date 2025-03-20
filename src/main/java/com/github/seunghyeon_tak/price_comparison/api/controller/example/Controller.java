package com.github.seunghyeon_tak.price_comparison.api.controller.example;

import com.github.seunghyeon_tak.price_comparison.common.annotation.Loggable;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.request.example.DtoRequest;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.example.DtoResponse;
import com.github.seunghyeon_tak.price_comparison.common.response.Api;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class Controller {
    @GetMapping("/test")
    @Loggable("GET 테스트 컨트롤러")
    public String test() {
        return "test mannnn";
    }

    @PostMapping("/test")
    @Loggable("POST 테스트 컨트롤러")
    public Api<DtoResponse> test2(@RequestBody DtoRequest request) {
        DtoResponse response = DtoResponse.builder()
                .name(request.getName())
                .age(request.getAge())
                .build();
        return Api.success(response);
    }
}
