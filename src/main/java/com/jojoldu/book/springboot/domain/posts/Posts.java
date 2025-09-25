package com.jojoldu.book.springboot.domain.posts;

import com.jojoldu.book.springboot.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

//// 롬복의 어노테이션
//@Getter
//@NoArgsConstructor
//// JPA의 어노테이션
//@Entity
//public class Posts extends BaseTimeEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(length = 500, nullable = false)
//    private String title;
//
//    @Column(columnDefinition = "TEXT", nullable = false)
//    private String content;
//
//    private String author;
//
//    @Builder
//    public Posts(String title, String content, String author) {
//        this.title = title;
//        this.content = content;
//        this.author = author;
//    }
//
//    public void update(String title, String content) {
//        this.title = title;
//        this.content = content;
//    }
//}

@Getter
@NoArgsConstructor
@Entity
public class Posts extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(length = 100, nullable = false)
    private String model;        // 이 필드 추가

    @Column(nullable = false)
    private Boolean active = true;  // 이 필드 추가

    private String author;

    @Builder
    public Posts(String title, String content, String model, Boolean active, String author) {
        this.title = title;
        this.content = content;
        this.model = model;      // Builder에 추가
        this.active = active != null ? active : true;  // Builder에 추가
        this.author = author;
    }

    public void update(String title, String content, String model, Boolean active) {
        this.title = title;
        this.content = content;
        this.model = model;      // update 메서드에도 추가
        this.active = active;    // update 메서드에도 추가
    }
}