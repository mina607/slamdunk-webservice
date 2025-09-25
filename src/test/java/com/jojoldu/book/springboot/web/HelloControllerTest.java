package com.jojoldu.book.springboot.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 여러 스프링 테스트 어노테이션 중 web에 집중할 수 있는 어노테이션
@WebMvcTest(controllers = HelloController.class)
public class HelloControllerTest {
    // 스피링이 관리하는 빈(Bean)을 주입받습니다
    @Autowired
    //웹 API를 테스트 할 떄 사용합니다
    private MockMvc mvc;

    @Test
    public void hello가_리턴된다() throws Exception {
        String hello = "hello";

        mvc.perform(get("/hello"))
                //HTTP 해더의 status를 검증합니다.
                .andExpect(status().isOk())
                // mvc.perform의 결과를 검증합니다.
                .andExpect(content().string(hello));
    }
}

