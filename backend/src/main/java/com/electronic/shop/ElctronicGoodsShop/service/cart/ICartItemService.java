package com.electronic.shop.ElctronicGoodsShop.service.cart;

import com.electronic.shop.ElctronicGoodsShop.dtos.CartItemDto;
import com.electronic.shop.ElctronicGoodsShop.model.CartItem;

public interface ICartItemService {
    CartItem addItemToCart(Long cartId, Long productId, int quantity);
    void removeItemFromCart(Long cartId, Long productId);
    void updateItemQuantity(Long cartId, Long productId, int quantity);
    CartItem getCartItem(Long cartId, Long productId);

    CartItemDto convertToDto(CartItem cartItem);
}
