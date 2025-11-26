package com.electronic.shop.ElctronicGoodsShop.service.image;

import com.electronic.shop.ElctronicGoodsShop.dtos.ImageDto;
import com.electronic.shop.ElctronicGoodsShop.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long imageId);
    void deleteImageById(Long imageId);
    void updateImage(MultipartFile file, Long imageId);
    List<ImageDto> saveImages(Long productId, List<MultipartFile> files);

}
