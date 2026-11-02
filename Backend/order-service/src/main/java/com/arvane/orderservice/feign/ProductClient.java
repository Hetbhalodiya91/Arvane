package com.arvane.orderservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/api/v1/products/sku/{sku}")
    Map<String, Object> getProductBySku(@PathVariable("sku") String sku);

    @GetMapping("/api/v1/products/{id}")
    Map<String, Object> getProductById(@PathVariable("id") Long id);
}