package com.electronic.shop.ElctronicGoodsShop.request;

import lombok.Data;

@Data
public class PaymentRequest {
    private int amount;
    private String currency;
}
