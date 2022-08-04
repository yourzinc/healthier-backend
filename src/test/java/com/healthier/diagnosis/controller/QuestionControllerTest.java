package com.healthier.diagnosis.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthier.diagnosis.dto.DecisiveQuestionRequestDto;
import com.healthier.diagnosis.dto.FirstQuestionRequestDto;
import com.healthier.diagnosis.dto.QuestionRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class QuestionControllerTest {

    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void before(WebApplicationContext wac) {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @DisplayName("질문 조회")
    @Test
    void getNextQuestion() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/diagnose").contentType(MediaType.APPLICATION_JSON)
                .content(
                        objectMapper.writeValueAsString(new QuestionRequestDto(
                                "62ca494f705b0e3bdeefc747",
                                2
                                )
                        )))
                .andExpect(status().isOk());

    }

    @DisplayName("첫번째 질문 조회_yes")
    @Test
    void getFirstQuestion_yes() throws Exception {
        FirstQuestionRequestDto dto = FirstQuestionRequestDto.builder()
                .answer("y")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/diagnose/sleepdisorder/first").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

    }

    @DisplayName("첫번째 질문 조회_no")
    @Test
    void getFirstQuestion_no() throws Exception {
        FirstQuestionRequestDto dto = FirstQuestionRequestDto.builder()
                .answer("y")
                .scoreB(13)
                .gender("f")
                .birthYear(2000)
                .interests(Arrays.stream(new int[]{1, 2, 3, 4}).boxed().collect(Collectors.toList()))
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/diagnose/sleepdisorder/first").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

    }

    @DisplayName("결정적 질문 진단결과 조회")
    @Test
    void getDecisiveQuestion() throws Exception {
        DecisiveQuestionRequestDto dto = DecisiveQuestionRequestDto.builder()
                .questionId("62ca4970705b0e3bdeefc749")
                .answerId(1)
                .period(3)
                .scoreB(11)
                .birthYear(2000)
                .gender("f")
                .interests(Arrays.stream(new int[]{1, 2, 3, 4}).boxed().collect(Collectors.toList()))
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/diagnose/sleepdisorder/decisive").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }
}
