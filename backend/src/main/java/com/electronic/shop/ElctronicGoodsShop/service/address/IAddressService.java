package com.electronic.shop.ElctronicGoodsShop.service.address;


import com.electronic.shop.ElctronicGoodsShop.dtos.AddressDto;
import com.electronic.shop.ElctronicGoodsShop.model.Address;

import java.util.List;

public interface IAddressService {
    List<Address>  createAddress(List<Address> addressList, Long userId);
    List<Address> getUserAddresses(Long userId);
    Address getAddressById(Long addressId);
    void deleteAddress(Long addressId);
    Address updateAddress(Long id, Address address);

    List<AddressDto> convertToDto(List<Address> addressList);

    AddressDto convertToDto(Address address);
}
