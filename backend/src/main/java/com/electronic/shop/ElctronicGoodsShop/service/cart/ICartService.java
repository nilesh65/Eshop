package com.electronic.shop.ElctronicGoodsShop.service.cart;


import com.electronic.shop.ElctronicGoodsShop.dtos.CartDto;
import com.electronic.shop.ElctronicGoodsShop.model.Cart;
import com.electronic.shop.ElctronicGoodsShop.model.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long cartId);

    Cart getCartByUserId(Long userId);

    void clearCart(Long cartId);

    Cart initializeNewCartForUser(User user);

    BigDecimal getTotalPrice(Long cartId);

    CartDto convertToDto(Cart cart);
}
