package com.healthier.diagnosis.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthier.diagnosis.dto.headache.AdditionalFactorResultRequest;
import com.healthier.diagnosis.dto.headache.ResultDto;
import com.healthier.diagnosis.dto.headache.painArea.HeadachePainAreaFirstRequest;
import com.healthier.diagnosis.dto.headache.painArea.HeadachePainAreaNextRequest;
import com.healthier.diagnosis.dto.headache.result.HeadacheResultRequest;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class HeadacheQuestionControllerTest {
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    HeadacheQuestionController questionController;

    @BeforeEach
    void before(WebApplicationContext wac) {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @DisplayName("API : 특정 통증 부위 시작 질문 - 뒷목")
    @Test
    void painAreaFirstQuestion() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v2/diagnose/headache/pain-area/first").contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(new HeadachePainAreaFirstRequest("뒷목")
                                        )))
                .andExpect(result -> status().isOk());
    }

    @DisplayName("API : 특정 통증 부위 다음 질문 - 다음 질문")
    @Test
    public void painAreaNextQuestion() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v2/diagnose/headache/pain-area/next").contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(new HeadachePainAreaNextRequest(461,1)
                                )))
                .andExpect(result -> status().isOk());
    }

    @DisplayName("API : 특정 통증 부위 다음 질문 - 진단 결과")
    @Test
    public void painAreaNextQuestionResult() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v2/diagnose/headache/pain-area/next").contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(new HeadachePainAreaNextRequest(461,0)
                                )))
                .andExpect(result -> status().isOk());
    }

    @DisplayName("API : 추가적인 악화요인 질문")
    @Test
    public void additionalFactorQuestion() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v2/diagnose/headache/additional-factor").contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(questionController.AdditionalFactorQuestion()
                                )))
                .andExpect(result -> status().isOk());
    }

    @DisplayName("API : 추가적인 악화요인 결과")
    @Test
    public void additionalFactorResult() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v2/diagnose/headache/additional-factor").contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(new AdditionalFactorResultRequest(601, new int [] {2, 3} )
                                )))
                .andExpect(result -> status().isOk());
    }

    @DisplayName("API : 최종 문진 결과")
    @Test
    public void headacheResult() throws Exception {

        List<ResultDto> results = new ArrayList<>();

        results.add(new ResultDto(1002, "전조증상이 있는 편두통"));
        results.add(new ResultDto(1022, "대후두 신경통"));
        results.add(new ResultDto(1025, "약물 과용 두통"));

        HeadacheResultRequest request = HeadacheResultRequest.builder()
                .gender("f")
                .birthYear(2000)
                .interests(Arrays.asList(0))
                .tracks(Arrays.asList(
                            HeadacheResultRequest.Track.builder().questionId(412).answerId(Arrays.asList(1)).build(),
                            HeadacheResultRequest.Track.builder().questionId(412).answerId(Arrays.asList(0)).build(),
                            HeadacheResultRequest.Track.builder().questionId(413).answerId(Arrays.asList(1)).build(),
                            HeadacheResultRequest.Track.builder().questionId(414).answerId(Arrays.asList(0)).build(),
                            HeadacheResultRequest.Track.builder().questionId(415).answerId(Arrays.asList(0)).build()
                        )
                )
                .results(results)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v2/diagnose/headache/result").contentType(MediaType.APPLICATION_JSON)
                .content(
                        objectMapper.writeValueAsString(request)))
                .andExpect(result -> status().isOk());
    }
}