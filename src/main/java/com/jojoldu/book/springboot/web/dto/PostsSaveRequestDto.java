package com.jojoldu.book.springboot.web.dto;

import com.jojoldu.book.springboot.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//@Getter
//@NoArgsConstructor
//public class PostsSaveRequestDto {
//    private String title;
//    private String content;
//    private String author;
//
//    @Builder
//    public PostsSaveRequestDto(String title, String content, String author) {
//        this.title = title;
//        this.content = content;
//        this.author = author;
//    }
//
//    public Posts toEntity() {
//        return Posts.builder()
//                .title(title)
//                .content(content)
//                .author(author)
//                .build();
//    }
//
//}

@Getter
@NoArgsConstructor
public class PostsSaveRequestDto {
    private String title;
    private String content;
    private String model;
    private Boolean active;
    private String author;

    @Builder
    public PostsSaveRequestDto(String title, String content, String model, Boolean active, String author) {
        this.title = title;
        this.content = content;
        this.model = model;
        this.active = active;
        this.author = author;
    }

    public Posts toEntity() {
        return Posts.builder()
                .title(title)
                .content(content)
                .model(model)
                .active(active != null ? active : true)  // null인 경우 기본값 true
                .author(author)
                .build();
    }
}