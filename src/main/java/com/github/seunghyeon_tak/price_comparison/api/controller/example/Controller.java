package com.github.seunghyeon_tak.price_comparison.api.controller.example;

import com.github.seunghyeon_tak.price_comparison.common.dto.api.request.example.DtoRequest;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.example.DtoResponse;
import com.github.seunghyeon_tak.price_comparison.common.response.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class Controller {
    @GetMapping("/test")
    public String test() {
        return "test mannnn";
    }

    @PostMapping("/test")
    public Api<DtoResponse> test2(@RequestBody DtoRequest request) {
        log.info("controller 호출");
        System.out.println(request.getName());
        System.out.println(request.getAge());
        DtoResponse response = DtoResponse.builder()
                .name("mason test")
                .age(27)
                .build();
        return Api.success(response);
    }
}
