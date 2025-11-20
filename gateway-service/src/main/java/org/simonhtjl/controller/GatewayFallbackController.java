package org.simonhtjl.controller;

import org.simonhtjl.dto.ErrResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@RestController
public class GatewayFallbackController {

    @RequestMapping("/fallback")
    public Mono<ResponseEntity<ErrResponse>> fallback(ServerWebExchange exchange) {
        ErrResponse errorResponse = new ErrResponse(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "Service Unavailable",
                "The service is currently unavailable. Please try again later.",
                exchange.getRequest().getPath().value()
        );

        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse));
    }

    @RequestMapping("/")
    public Mono<ResponseEntity<String>> gatewayInfo() {
        String info = """
            ===================================
            SUPERMARKET POS - API GATEWAY
            ===================================
            Status: RUNNING
            Port: 8080
            Routes:
            - Auth Service: /auth/**
            - Product Service: /api/products/**
            - Sale Service: /api/sales/**
            - Report Service: /api/reports/**
            ===================================
            """;
        return Mono.just(ResponseEntity.ok(info));
    }
}
