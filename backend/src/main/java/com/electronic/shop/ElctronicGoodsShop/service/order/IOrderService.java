package com.electronic.shop.ElctronicGoodsShop.service.order;


import com.electronic.shop.ElctronicGoodsShop.dtos.OrderDto;
import com.electronic.shop.ElctronicGoodsShop.model.Order;
import com.electronic.shop.ElctronicGoodsShop.request.PaymentRequest;
import com.stripe.exception.StripeException;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);
    List<OrderDto> getUserOrders(Long userId);

    String createPaymentIntent(PaymentRequest request) throws StripeException;

    OrderDto convertToDto(Order order);
    Order placeBuyNowOrder(Long userId, Long productId, int quantity);

}
