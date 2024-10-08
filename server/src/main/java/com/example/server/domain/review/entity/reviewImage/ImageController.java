package com.example.server.domain.review.entity.reviewImage;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @GetMapping("/reviewImages/{image-id}")
    public ResponseEntity<byte[]> getImage(@PathVariable("image-id") Long imageId){
        Image image = imageService.findImage(imageId);
        byte[] imageByteArray = image.getImage();
        HttpHeaders headers = new HttpHeaders();
        // 각 이미지 타입(jpg, png)에 알맞도록, header에 ContentType입력
        headers.setContentType(MediaType.valueOf(image.getType()));

        return new ResponseEntity<byte[]>(imageByteArray,headers, HttpStatus.OK);
    }

}
