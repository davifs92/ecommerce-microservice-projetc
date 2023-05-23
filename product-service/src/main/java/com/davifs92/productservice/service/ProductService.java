package com.davifs92.productservice.service;

import com.davifs92.productservice.dto.ProductRequest;
import com.davifs92.productservice.dto.ProductResponse;
import com.davifs92.productservice.model.Product;
import com.davifs92.productservice.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    public void createProduct(ProductRequest productRequest){
        Product product = Product.builder()
                .description(productRequest.getDescription())
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .build();
        Product saved = productRepository.save(product);
        log.info("Product {} is created", saved.getId());

    }


    public List<ProductResponse> getAllProducts() {
        List<Product> products =  productRepository.findAll();

      List<ProductResponse> productResponse =  products.stream()
                .map(product -> mapToProductResponse(product))
                .collect(Collectors.toList());

      return productResponse;

    }

    private ProductResponse mapToProductResponse(Product product){
        ProductResponse productResponse = ProductResponse.builder()
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .id(product.getId())
                .build();

        return productResponse;



    }

}
