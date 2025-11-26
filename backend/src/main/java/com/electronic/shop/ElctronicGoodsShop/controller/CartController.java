package com.electronic.shop.ElctronicGoodsShop.controller;


import com.electronic.shop.ElctronicGoodsShop.dtos.CartDto;
import com.electronic.shop.ElctronicGoodsShop.model.Cart;
import com.electronic.shop.ElctronicGoodsShop.response.ApiResponse;
import com.electronic.shop.ElctronicGoodsShop.service.cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/carts")
public class CartController {
    private final ICartService cartService;

    @GetMapping("/user/{userId}/cart")
    public ResponseEntity<ApiResponse> getUserCart(@PathVariable Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        CartDto cartDto = cartService.convertToDto(cart);
        return ResponseEntity.ok(new ApiResponse("Success", cartDto));
    }

    @DeleteMapping("/cart/{cartId}/clear")
    public void clearCart(@PathVariable Long cartId) {
        cartService.clearCart(cartId);
    }
}
