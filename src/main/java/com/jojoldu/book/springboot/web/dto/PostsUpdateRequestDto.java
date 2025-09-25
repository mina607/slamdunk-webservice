package com.jojoldu.book.springboot.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//@Getter
//@NoArgsConstructor
//public class PostsUpdateRequestDto {
//    private String title;
//    private String content;
//
//    @Builder
//    public PostsUpdateRequestDto(String title, String content) {
//        this.title = title;
//        this.content = content;
//    }
//}

@Getter
@NoArgsConstructor
public class PostsUpdateRequestDto {
    private String title;
    private String content;
    private String model;
    private Boolean active;

    @Builder
    public PostsUpdateRequestDto(String title, String content, String model, Boolean active) {
        this.title = title;
        this.content = content;
        this.model = model;
        this.active = true;
    }

}