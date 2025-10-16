// src/main/java/com/jojoldu/book/springboot/web/dto/AdminLoginRequest.java

package com.jojoldu.book.springboot.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor // 생성자가 필요할 수 있으니 추가해 줍니다.
public class AdminLoginRequest {
    private String username;
    private String password;
}