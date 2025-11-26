package com.electronic.shop.ElctronicGoodsShop.controller;


import com.electronic.shop.ElctronicGoodsShop.dtos.OrderDto;
import com.electronic.shop.ElctronicGoodsShop.model.Order;
import com.electronic.shop.ElctronicGoodsShop.request.PaymentRequest;
import com.electronic.shop.ElctronicGoodsShop.response.ApiResponse;
import com.electronic.shop.ElctronicGoodsShop.service.order.IOrderService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    private final IOrderService orderService;

    @PostMapping("/user/{userId}/place-order")
    public ResponseEntity<ApiResponse> placeOrder(@PathVariable Long userId){
        Order order = orderService.placeOrder(userId);
        OrderDto orderDto =  orderService.convertToDto(order);
        return ResponseEntity.ok(new ApiResponse("Order placed successfully!", orderDto));
    }
    //Assignment 6
    // Create an order DTO to return a user orders;

    @GetMapping("/user/{userId}/orders")
    private ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId){
        List<OrderDto> orders = orderService.getUserOrders(userId);
        return ResponseEntity.ok(new ApiResponse("Success!", orders));
    }
    
    //Assignment 7
    // Create an order DTO to return a list user orders;
    @PostMapping("/create-payment-intent")
    public ResponseEntity<?> createPaymentIntent(@RequestBody PaymentRequest request) throws StripeException {
        String clientSecret = orderService.createPaymentIntent(request);
        return ResponseEntity.ok(Map.of("clientSecret",clientSecret));
    }
    @PostMapping("/user/{userId}/buy-now/{productId}")
    public ResponseEntity<ApiResponse> buyNow(
            @PathVariable Long userId,
            @PathVariable Long productId,
            @RequestParam int quantity) {

        Order order = orderService.placeBuyNowOrder(userId, productId, quantity);
        OrderDto orderDto = orderService.convertToDto(order);
        return ResponseEntity.ok(new ApiResponse("Buy-Now order placed successfully!", orderDto));
    }

}
