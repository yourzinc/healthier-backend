package com.healthier.diagnosis.service;

import com.healthier.diagnosis.domain.question.Question;
import com.healthier.diagnosis.dto.*;
import com.healthier.diagnosis.repository.QuestionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;

import java.util.*;
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
        assertThat(question.getQuestion().getId()).isEqualTo("62ca497e705b0e3bdeefc74a");

    }

//    @DisplayName("첫번째 질문 조회 - yes")
//    @Test
//    void findFirstQuestion_yes() {
//        //given
//        FirstQuestionRequestDto dto = FirstQuestionRequestDto.builder()
//                .answer("y")
//                .build();
//
//        //when
//        QuestionResponseDto question = (QuestionResponseDto) questionService.findFirstQuestion(dto);
//
//        //then
//        assertThat(question.getQuestion().getQuestion()).isEqualTo("자신을 가장 잘 설명하는 증상을 골라주세요");
//    }
//
//    @DisplayName("첫번째 질문 조회 - no")
//    @Test
//    void findFirstQuestion_no() {
//        //given
//        FirstQuestionRequestDto dto = FirstQuestionRequestDto.builder()
//                .answer("n")
//                .scoreB(3)
//                .birthYear(2000)
//                .gender("f")
//                .interests(Arrays.stream(new int[]{1, 2, 3, 4}).boxed().collect(Collectors.toList()))
//                .build();
//
//        //when
//        DiagnosisResponseDto diagnosis = (DiagnosisResponseDto) questionService.findFirstQuestion(dto);
//
//        //then
//        assertThat(diagnosis.getDiagnosticResult().getH1()).isEqualTo("수면의 문제가 일상에 영향을 주지 않는다면");
//    }

    @DisplayName("결정적 질문 진단결과 조회")
    @Test
    void findDecisiveQuestion() {
        //given
        DecisiveQuestionRequestDto dto = DecisiveQuestionRequestDto.builder()
                .questionId("62ca4970705b0e3bdeefc749")
                .answerId(1)
                .period(3)
                .scoreB(11)
                .build();
                
        //when
        DiagnosisResponseDto diagnosis = (DiagnosisResponseDto) questionService.findDecisiveQuestion(dto);

        //then
        assertThat(diagnosis.getDiagnosticResult().getTitle()).isEqualTo("일주기 리듬 수면장애");
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
                .period(0) // 기간 : 상관없음
                .cycle(0) // 주기 : 상관없음
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
                .period(0) // 기간 : 상관없음
                .cycle(0) // 주기 : 상관없음
                .pain_level(3) // 통증의 정도 :  3 or 4
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
        List<HeadacheDecisiveQuestionRequestDto.Track> tracks = new ArrayList<>();

        List<Integer> answers = new ArrayList<>();
        answers.add(0);

        HeadacheDecisiveQuestionRequestDto.Track track = new HeadacheDecisiveQuestionRequestDto.Track("62d8e2fba49a42d5112f23c4", answers);
        tracks.add(track);

        HeadacheDecisiveQuestionRequestDto dto = HeadacheDecisiveQuestionRequestDto.builder()
                .questionId("62d8e2fba49a42d5112f23c4")
                .answerId(1) // 아니오
                .period(3) // 기간 : 한달 전(2) or 3개월 전(3)
                .cycle(1) // 주기 : 예(1)
                .pain_level(5) // 통증의 정도 : 상관없음 (1-5)
                .is_taking_medication(0) // 약물복용 X
                .birthYear(2000)
                .gender("f")
                .interests(Arrays.stream(new int[]{1, 2, 3, 4}).boxed().collect(Collectors.toList()))
                .tracks(tracks)
                .build();

        // when
        DiagnosisResponseDto diagnosis = (DiagnosisResponseDto) questionService.findHeadacheDecisiveQuestion(dto);

        // then
        assertThat(diagnosis.getDiagnosticResult().getTitle()).isEqualTo("만성 두통");
    }

    // 수면장애 초기 질문
    @DisplayName("수면장애 초기 질문")
    @Test
    void getSleepdisorderFirstQuestion(){
        // when
        QuestionDto first_question = questionService.findFirstQuestion("sleepdisorder");

        // then
        assertThat(first_question.getQuestion()).isEqualTo("언제부터 잠이 안 오기 시작했나요?");
    }

    // 두통 초기 질문
    @DisplayName("두통 초기 질문")
    @Test
    void getHeadacheFirstQuestion(){
        // when
        QuestionDto first_question = questionService.findFirstQuestion("headache");

        // then
        assertThat(first_question.getQuestion()).isEqualTo("언제부터 통증이 시작되었나요?");
    }

    // 두통 마지막 초기 질문 응답
    @DisplayName("두통 마지막 초기 질문 응답")
    @Test
    void findHeadacheDefaultQuestionAfter(){
        QuestionResponseDto default_question_after = questionService.findHeadacheDefaultQuestionAfter(
                HeadacheDefaultQuestionAfterRequestDto.builder()
                        .siteId(1) // 진단 부위 (1) : 관자놀이
                        .build());

        assertThat(default_question_after.getQuestion().getQuestion()).isEqualTo("맥박이 느껴지는 것 같은 박동성 통증이 느껴지나요?");
    }
}