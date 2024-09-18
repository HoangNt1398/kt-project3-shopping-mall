package com.example.server.domain.review.entity.reviewImage;

import com.example.server.exception.BusinessLogicException;
import com.example.server.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    public Image findImage(Long imageId){
        Optional<Image> optionalImage = imageRepository.findById(imageId);
        Image findImage = optionalImage.orElseThrow(() -> {
            throw new BusinessLogicException(ExceptionCode.IMAGE_NOT_FOUND);
        });
        return findImage;
    }
}
