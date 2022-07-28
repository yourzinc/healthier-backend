package com.healthier.diagnosis.service;

import com.healthier.diagnosis.domain.question.Question;
import com.healthier.diagnosis.dto.*;
import com.healthier.diagnosis.repository.QuestionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QuestionServiceTest {
    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    QuestionService questionService;

    @DisplayName("질문 조회")
    @Test
    void findNextQuestion() {
        //given
        QuestionRequestDto dto = QuestionRequestDto.builder()
                .questionId("62ca494f705b0e3bdeefc747")
                .answerId(2)
                .build();

        //when
        QuestionResponseDto question = questionService.findNextQuestion(dto);

        //then
        assertThat(question.getQuestion().getId()).isEqualTo("62ca49bf705b0e3bdeefc74d");

    }

    @DisplayName("첫번째 질문 조회 - yes")
    @Test
    void findFirstQuestion_yes() {
        //given
        FirstQuestionRequestDto dto = FirstQuestionRequestDto.builder()
                .answer("y")
                .build();

        //when
        QuestionResponseDto question = (QuestionResponseDto) questionService.findFirstQuestion(dto);

        //then
        assertThat(question.getQuestion().getQuestion()).isEqualTo("자신을 가장 잘 설명하는 증상을 골라주세요");
    }

    @DisplayName("첫번째 질문 조회 - no")
    @Test
    void findFirstQuestion_no() {
        //given
        FirstQuestionRequestDto dto = FirstQuestionRequestDto.builder()
                .answer("n")
                .scoreB(3)
                .birthYear(2000)
                .gender("f")
                .interests(Arrays.stream(new int[]{1, 2, 3, 4}).boxed().collect(Collectors.toList()))
                .build();

        //when
        DiagnosisResponseDto diagnosis = (DiagnosisResponseDto) questionService.findFirstQuestion(dto);

        //then
        assertThat(diagnosis.getDiagnosticResult().getH1()).isEqualTo("수면의 문제가 일상에 영향을 주지 않는다면");
    }

    @DisplayName("두통 결정적 진단 응답 - 약물과용 두통")
    @Test
    void 약물과용두통(){
        // given
        HeadacheDecisiveQuestionRequestDto dto = HeadacheDecisiveQuestionRequestDto.builder()
                .questionId("62d8e2fba49a42d5112f23c4")
                .answerId(1) // 아니오
                .period(0)
                .cycle(0)
                .pain_level(1)
                .is_taking_medication(1) // 약물복용 O
                .birthYear(2000)
                .gender("f")
                .interests(Arrays.stream(new int[]{1, 2, 3, 4}).boxed().collect(Collectors.toList()))
                .build();

        // when
        DiagnosisResponseDto diagnosis = (DiagnosisResponseDto) questionService.findHeadacheDecisiveQuestion(dto);

        // then
        assertThat(diagnosis.getDiagnosticResult().getTitle()).isEqualTo("약물 과용 두통");
    }

    @DisplayName("두통 결정적 진단 응답 - 경미한 두통")
    @Test
    void 경미한두통(){
        // given
        HeadacheDecisiveQuestionRequestDto dto = HeadacheDecisiveQuestionRequestDto.builder()
                .questionId("62d8e2fba49a42d5112f23c4")
                .answerId(1) // 아니오
                .period(0)
                .cycle(0)
                .pain_level(1) // 경미
                .is_taking_medication(0) // 약물복용 X
                .birthYear(2000)
                .gender("f")
                .interests(Arrays.stream(new int[]{1, 2, 3, 4}).boxed().collect(Collectors.toList()))
                .build();

        // when
        DiagnosisResponseDto diagnosis = (DiagnosisResponseDto) questionService.findHeadacheDecisiveQuestion(dto);

        // then
        assertThat(diagnosis.getDiagnosticResult().getTitle()).isEqualTo("경미한 두통");
    }

    @DisplayName("두통 결정적 진단 응답 - 중증 두통")
    @Test
    void 중증두통(){
        // given
        HeadacheDecisiveQuestionRequestDto dto = HeadacheDecisiveQuestionRequestDto.builder()
                .questionId("62d8e2fba49a42d5112f23c4")
                .answerId(1) // 아니오
                .period(0)
                .cycle(0)
                .pain_level(3) // 주의
                .is_taking_medication(0) // 약물복용 X
                .birthYear(2000)
                .gender("f")
                .interests(Arrays.stream(new int[]{1, 2, 3, 4}).boxed().collect(Collectors.toList()))
                .build();

        // when
        DiagnosisResponseDto diagnosis = (DiagnosisResponseDto) questionService.findHeadacheDecisiveQuestion(dto);

        // then
        assertThat(diagnosis.getDiagnosticResult().getTitle()).isEqualTo("중증 두통");
    }

    @DisplayName("두통 결정적 진단 응답 - 만성 두통")
    @Test
    void 만성두통(){
        // given
        HeadacheDecisiveQuestionRequestDto dto = HeadacheDecisiveQuestionRequestDto.builder()
                .questionId("62d8e2fba49a42d5112f23c4")
                .answerId(1) // 아니오
                .period(0)
                .cycle(0)
                .pain_level(5) // 심각
                .is_taking_medication(0) // 약물복용 X
                .birthYear(2000)
                .gender("f")
                .interests(Arrays.stream(new int[]{1, 2, 3, 4}).boxed().collect(Collectors.toList()))
                .build();

        // when
        DiagnosisResponseDto diagnosis = (DiagnosisResponseDto) questionService.findHeadacheDecisiveQuestion(dto);

        // then
        assertThat(diagnosis.getDiagnosticResult().getTitle()).isEqualTo("만성 두통");
    }
}