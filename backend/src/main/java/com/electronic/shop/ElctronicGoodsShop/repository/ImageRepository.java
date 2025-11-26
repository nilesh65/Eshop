package com.electronic.shop.ElctronicGoodsShop.repository;

import com.electronic.shop.ElctronicGoodsShop.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByProductId(Long id);
}
