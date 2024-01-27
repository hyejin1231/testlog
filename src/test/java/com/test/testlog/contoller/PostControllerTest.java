package com.test.testlog.contoller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("/posts 요청 시 Hello World를 출력한다.")
    void posts() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"title\": \"제목입니다.\", \"content\": \"내용입니다.\"}") // JSON 형태
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("{}"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("/posts 요청 시 title 값은 필수다. Title 값이 없으면 에러 메시지를 리턴한다.")
    void posts2() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"title\": \"\", \"content\": \"내용입니다.\"}") // JSON 형태
                ).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.validation.title").value("title 값은 필수입니다."))
                .andDo(MockMvcResultHandlers.print());
    }
}