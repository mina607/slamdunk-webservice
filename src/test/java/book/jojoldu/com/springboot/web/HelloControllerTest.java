package book.jojoldu.com.springboot.web;

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

    // 스프링이 관리하는 빈(Bean)을 주입받는다.
    @Autowired
    // 웹 api를 태스트할 때 사용
    private MockMvc mvc;

    @Test
    public void hello가_리턴된다() throws Exception {
        String hello = "hello";

        mvc.perform(get("/hello"))
                // HTTP 헤더의 status를 검증한다.
                .andExpect(status().isOk())
                // mvc.perform의 결과를 검증한다.
                .andExpect(content().string(hello));
    }
}
