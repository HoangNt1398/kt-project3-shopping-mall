package com.example.server.domain.product.entity.productImage;

import com.example.server.domain.product.service.ProductService;
import com.example.server.exception.BusinessLogicException;
import com.example.server.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductImageService {
    private final ProductImageRepository productImageRepository;
    private final ProductService productService;

    public ProductDetailImage findProductDetailImage(Long pdImageId){
        Optional<ProductDetailImage> optionalPdImage = productImageRepository.findById(pdImageId);
        ProductDetailImage findPdImage = optionalPdImage.orElseThrow(() -> {
            throw new BusinessLogicException(ExceptionCode.IMAGE_NOT_FOUND);
        });

        productService.verifiedActiveProduct(findPdImage.getProduct());
        return findPdImage;
    }
}
