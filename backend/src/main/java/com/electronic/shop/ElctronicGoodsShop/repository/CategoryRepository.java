package com.electronic.shop.ElctronicGoodsShop.repository;

import com.electronic.shop.ElctronicGoodsShop.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);

    boolean existsByName(String name);
}
