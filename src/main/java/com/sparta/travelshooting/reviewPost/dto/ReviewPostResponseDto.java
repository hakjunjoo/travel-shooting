package com.sparta.travelshooting.reviewPost.dto;


import com.sparta.travelshooting.reviewPost.entity.ReviewPost;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewPostResponseDto {
    private String title;
    private String content;
    private String imageUrl;



    public ReviewPostResponseDto(ReviewPost reviewPost){
        this.title = reviewPost.getTitle();
        this.content = reviewPost.getContent();
        //이미지값을 넣지 않을 때를 위한 로직
        if(reviewPost.getImage() != null){
            this.imageUrl = reviewPost.getImage().getAccessUrl();
        }else{
            this.imageUrl = null;
        }
    }
}


