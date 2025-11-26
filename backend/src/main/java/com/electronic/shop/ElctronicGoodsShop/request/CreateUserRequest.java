package com.electronic.shop.ElctronicGoodsShop.request;


import com.electronic.shop.ElctronicGoodsShop.model.Address;
import lombok.Data;

import java.util.List;

@Data
public class CreateUserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<Address> addressList;
}
