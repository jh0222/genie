package com.genie.controller;

import com.genie.entity.Board;
import com.genie.repository.BoardRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.yml")
class BoardControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    BoardRepository boardRepository;
    @Test
    @DisplayName("게시판 등록 페이지 권한 테스트")
    @WithMockUser(username = "user", roles = "USER")
    public void boardFormTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/board/new"))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    @DisplayName("게시판 등록 페이지 일반 회원 접근 테스트")
    @WithMockUser(username = "guest", roles = "GUEST")
    public void boardFormNotUserTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/board/new"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

}