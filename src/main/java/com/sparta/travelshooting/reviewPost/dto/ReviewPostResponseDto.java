package com.sparta.travelshooting.reviewPost.dto;


import com.sparta.travelshooting.S3Image.entity.Image;
import com.sparta.travelshooting.reviewPost.entity.ReviewPost;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ReviewPostResponseDto {
    private String title;
    private String content;
    private List<String> imageUrls;
    private Integer likeCounts;


    public ReviewPostResponseDto(ReviewPost reviewPost){
        this.title = reviewPost.getTitle();
        this.content = reviewPost.getContent();
        this.likeCounts = reviewPost.getLikeCounts();
        //이미지값을 넣지 않을 때를 위한 로직
        this.imageUrls = reviewPost.getImages().stream()
                .map(Image::getAccessUrl)
                .collect(Collectors.toList());
    }
    }



