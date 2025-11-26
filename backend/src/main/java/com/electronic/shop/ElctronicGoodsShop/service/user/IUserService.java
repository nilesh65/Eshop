package com.electronic.shop.ElctronicGoodsShop.service.user;


import com.electronic.shop.ElctronicGoodsShop.dtos.UserDto;
import com.electronic.shop.ElctronicGoodsShop.model.User;
import com.electronic.shop.ElctronicGoodsShop.request.CreateUserRequest;
import com.electronic.shop.ElctronicGoodsShop.request.UserUpdateRequest;

public interface IUserService {
    User createUser(CreateUserRequest request);

    User updateUser(UserUpdateRequest request, Long userId);

    User getUserById(Long userId);

    void deleteUser(Long userId);

    UserDto convertUserToDto(User user);

    User getAuthenticatedUser();
}
