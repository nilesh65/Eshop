package com.electronic.shop.ElctronicGoodsShop.controller;


import com.electronic.shop.ElctronicGoodsShop.dtos.CartItemDto;
import com.electronic.shop.ElctronicGoodsShop.model.Cart;
import com.electronic.shop.ElctronicGoodsShop.model.CartItem;
import com.electronic.shop.ElctronicGoodsShop.model.User;
import com.electronic.shop.ElctronicGoodsShop.response.ApiResponse;
import com.electronic.shop.ElctronicGoodsShop.service.cart.ICartItemService;
import com.electronic.shop.ElctronicGoodsShop.service.cart.ICartService;
import com.electronic.shop.ElctronicGoodsShop.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {
    private final ICartItemService cartItemService;
    private final IUserService userService;
    private final ICartService cartService;

    @PostMapping("/item/add")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestParam Long productId, @RequestParam int quantity) {
        User user = userService.getAuthenticatedUser();
        Cart cart = cartService.initializeNewCartForUser(user);
        CartItem cartItem = cartItemService.addItemToCart(cart.getId(), productId, quantity);
        CartItemDto cartItemDto = cartItemService.convertToDto(cartItem);
        return ResponseEntity.ok(new ApiResponse("Item added successfully!", cartItemDto));
    }

    @DeleteMapping("/cart/{cartId}/item/{itemId}/remove")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId,
                                                          @PathVariable Long itemId) {
        cartItemService.removeItemFromCart(cartId, itemId);
        return ResponseEntity.ok(new ApiResponse("Item removed successfully!", null));
    }

    @PutMapping("/cart/{cartId}/item/{itemId}/update")
    public ResponseEntity<ApiResponse> updateCartItem(@PathVariable Long cartId,
                                                      @PathVariable Long itemId,
                                                      @RequestParam int quantity) {

        cartItemService.updateItemQuantity(cartId, itemId, quantity);
        return ResponseEntity.ok(new ApiResponse("Item updated successfully!", null));
    }
}
