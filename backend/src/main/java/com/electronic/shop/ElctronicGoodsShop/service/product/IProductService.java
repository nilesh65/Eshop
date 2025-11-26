package com.electronic.shop.ElctronicGoodsShop.service.product;


import com.electronic.shop.ElctronicGoodsShop.dtos.ProductDto;
import com.electronic.shop.ElctronicGoodsShop.model.Product;
import com.electronic.shop.ElctronicGoodsShop.request.AddProductRequest;
import com.electronic.shop.ElctronicGoodsShop.request.ProductUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IProductService {
    Product addProduct(AddProductRequest product);

    Product updateProduct(ProductUpdateRequest product, Long productId);

    Product getProductById(Long productId);

    void deleteProductById(Long productId);

    List<Product> getAllProducts();

    List<Product> getProductsByCategoryAndBrand(String category, String brand);

    List<Product> getProductsByCategory(String category);

    List<Product> getProductsByBrandAndName(String brand, String name);

    List<Product> getProductsByBrand(String brand);

    List<Product> getProductsByName(String name);

    List<Product> findDistinctProductsByName();

    List<String> getAllDistinctBrands();

    List<ProductDto> getConvertedProducts(List<Product> products);

    ProductDto convertToDto(Product product);

    List<Product> getProductsByCategoryId(Long categoryId);
    List<Product> searchProductsByImage(MultipartFile image) throws IOException;
}
