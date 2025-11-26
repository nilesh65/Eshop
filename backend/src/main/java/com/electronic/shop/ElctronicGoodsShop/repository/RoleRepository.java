package com.electronic.shop.ElctronicGoodsShop.repository;

import com.electronic.shop.ElctronicGoodsShop.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String userRole);
}
