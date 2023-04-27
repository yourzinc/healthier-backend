package com.healthier.diagnosis.service;

import com.healthier.diagnosis.domain.user.Track;
import com.healthier.diagnosis.dto.question.*;
import com.healthier.diagnosis.dto.question.decisiveQuestion.DecisiveQuestionRequestDto;
import com.healthier.diagnosis.dto.question.headache.HeadacheDefaultQuestionAfterRequestDto;
import com.healthier.diagnosis.repository.QuestionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

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
        DecisiveQuestionRequestDto dto = DecisiveQuestionRequestDto.builder()
                .questionId("62d8e2fba49a42d5112f23c4")
                .answerId(1) // 아니오
                .birthYear(2000)
                .gender("f")
                .interests(Arrays.asList(0))
                .tracks(Arrays.asList(
                        Track.builder().question_id("631adb3775ce6608eb91f761").answer_id(Arrays.asList(1)).build(), // 언제부터 통증이
                        Track.builder().question_id("631adb1775ce6608eb91f760").answer_id(Arrays.asList(1)).build(), // 한달에 15일 이상
                        Track.builder().question_id("631adaf175ce6608eb91f75f").answer_id(Arrays.asList(1)).build(), // 통증의 정도
                        Track.builder().question_id("631adabc75ce6608eb91f75e").answer_id(Arrays.asList(0)).build(), // 진통제?
                        Track.builder().question_id("62d8e1b4a49a42d5112f23b4").answer_id(Arrays.asList(1)).build(), // 맥박이 느껴지는
                        Track.builder().question_id("62d8e235a49a42d5112f23b7").answer_id(Arrays.asList(1)).build(), // 턱이 아프거나
                        Track.builder().question_id("62d8e2a5a49a42d5112f23be").answer_id(Arrays.asList(1)).build(), // 두통이 올때
                        Track.builder().question_id("62d8e2b1a49a42d5112f23bf").answer_id(Arrays.asList(1)).build(), // 고혈압이나
                        Track.builder().question_id("62d8e2c8a49a42d5112f23c1").answer_id(Arrays.asList(1)).build(), // 근육이 동반
                        Track.builder().question_id("62d8e2d5a49a42d5112f23c2").answer_id(Arrays.asList(1)).build(),  // 최근 시력이
                        Track.builder().question_id("62d8e2e4a49a42d5112f23c3").answer_id(Arrays.asList(1)).build(),  // 안면 마비나
                        Track.builder().question_id("62d8e2fba49a42d5112f23c4").answer_id(Arrays.asList(1)).build())) // 이명이나
                .build();

        // when
        DiagnosisResponseDto diagnosis = questionService.findHeadacheDecisiveQuestion(dto);

        // then
        assertThat(diagnosis.getDiagnosticResult().getTitle()).isEqualTo("약물 과용 두통");
    }

    @DisplayName("두통 결정적 진단 응답 - 경미한 두통")
    @Test
    void 경미한두통(){
        // given
        DecisiveQuestionRequestDto dto = DecisiveQuestionRequestDto.builder()
                .questionId("62d8e2fba49a42d5112f23c4")
                .answerId(1) // 아니오
                .birthYear(2000)
                .gender("f")
                .interests(Arrays.asList(0))
                .tracks(Arrays.asList(
                        Track.builder().question_id("631adb3775ce6608eb91f761").answer_id(Arrays.asList(1)).build(), // 언제부터 통증이
                        Track.builder().question_id("631adb1775ce6608eb91f760").answer_id(Arrays.asList(1)).build(), // 한달에 15일 이상
                        Track.builder().question_id("631adaf175ce6608eb91f75f").answer_id(Arrays.asList(1)).build(), // 통증의 정도
                        Track.builder().question_id("631adabc75ce6608eb91f75e").answer_id(Arrays.asList(1)).build(), // 진통제?
                        Track.builder().question_id("62d8e1b4a49a42d5112f23b4").answer_id(Arrays.asList(1)).build(), // 맥박이 느껴지는
                        Track.builder().question_id("62d8e235a49a42d5112f23b7").answer_id(Arrays.asList(1)).build(), // 턱이 아프거나
                        Track.builder().question_id("62d8e2a5a49a42d5112f23be").answer_id(Arrays.asList(1)).build(), // 두통이 올때
                        Track.builder().question_id("62d8e2b1a49a42d5112f23bf").answer_id(Arrays.asList(1)).build(), // 고혈압이나
                        Track.builder().question_id("62d8e2c8a49a42d5112f23c1").answer_id(Arrays.asList(1)).build(), // 근육이 동반
                        Track.builder().question_id("62d8e2d5a49a42d5112f23c2").answer_id(Arrays.asList(1)).build(),  // 최근 시력이
                        Track.builder().question_id("62d8e2e4a49a42d5112f23c3").answer_id(Arrays.asList(1)).build(),  // 안면 마비나
                        Track.builder().question_id("62d8e2fba49a42d5112f23c4").answer_id(Arrays.asList(1)).build())) // 이명이나
                .build();

        // when
        DiagnosisResponseDto diagnosis = questionService.findHeadacheDecisiveQuestion(dto);

        // then
        assertThat(diagnosis.getDiagnosticResult().getTitle()).isEqualTo("경미한 두통");
    }

    @DisplayName("두통 결정적 진단 응답 - 중증 두통")
    @Test
    void 중증두통(){
        // given
        DecisiveQuestionRequestDto dto = DecisiveQuestionRequestDto.builder()
                .questionId("62d8e2fba49a42d5112f23c4")
                .answerId(1) // 아니오
                .birthYear(2000)
                .gender("f")
                .interests(Arrays.asList(0))
                .tracks(Arrays.asList(
                        Track.builder().question_id("631adb3775ce6608eb91f761").answer_id(Arrays.asList(1)).build(), // 언제부터 통증이
                        Track.builder().question_id("631adb1775ce6608eb91f760").answer_id(Arrays.asList(1)).build(), // 한달에 15일 이상
                        Track.builder().question_id("631adaf175ce6608eb91f75f").answer_id(Arrays.asList(3)).build(), // 통증의 정도
                        Track.builder().question_id("631adabc75ce6608eb91f75e").answer_id(Arrays.asList(1)).build(), // 진통제?
                        Track.builder().question_id("62d8e1b4a49a42d5112f23b4").answer_id(Arrays.asList(1)).build(), // 맥박이 느껴지는
                        Track.builder().question_id("62d8e235a49a42d5112f23b7").answer_id(Arrays.asList(1)).build(), // 턱이 아프거나
                        Track.builder().question_id("62d8e2a5a49a42d5112f23be").answer_id(Arrays.asList(1)).build(), // 두통이 올때
                        Track.builder().question_id("62d8e2b1a49a42d5112f23bf").answer_id(Arrays.asList(1)).build(), // 고혈압이나
                        Track.builder().question_id("62d8e2c8a49a42d5112f23c1").answer_id(Arrays.asList(1)).build(), // 근육이 동반
                        Track.builder().question_id("62d8e2d5a49a42d5112f23c2").answer_id(Arrays.asList(1)).build(),  // 최근 시력이
                        Track.builder().question_id("62d8e2e4a49a42d5112f23c3").answer_id(Arrays.asList(1)).build(),  // 안면 마비나
                        Track.builder().question_id("62d8e2fba49a42d5112f23c4").answer_id(Arrays.asList(1)).build())) // 이명이나
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
        DecisiveQuestionRequestDto dto = DecisiveQuestionRequestDto.builder()
                .questionId("62d8e2fba49a42d5112f23c4")
                .answerId(1) // 아니오
                .birthYear(2000)
                .gender("f")
                .interests(Arrays.asList(0))
                .tracks(Arrays.asList(
                        Track.builder().question_id("631adb3775ce6608eb91f761").answer_id(Arrays.asList(2)).build(), // 언제부터 통증이
                        Track.builder().question_id("631adb1775ce6608eb91f760").answer_id(Arrays.asList(0)).build(), // 한달에 15일 이상
                        Track.builder().question_id("631adaf175ce6608eb91f75f").answer_id(Arrays.asList(5)).build(), // 통증의 정도
                        Track.builder().question_id("631adabc75ce6608eb91f75e").answer_id(Arrays.asList(1)).build(), // 진통제?
                        Track.builder().question_id("62d8e1b4a49a42d5112f23b4").answer_id(Arrays.asList(1)).build(), // 맥박이 느껴지는
                        Track.builder().question_id("62d8e235a49a42d5112f23b7").answer_id(Arrays.asList(1)).build(), // 턱이 아프거나
                        Track.builder().question_id("62d8e2a5a49a42d5112f23be").answer_id(Arrays.asList(1)).build(), // 두통이 올때
                        Track.builder().question_id("62d8e2b1a49a42d5112f23bf").answer_id(Arrays.asList(1)).build(), // 고혈압이나
                        Track.builder().question_id("62d8e2c8a49a42d5112f23c1").answer_id(Arrays.asList(1)).build(), // 근육이 동반
                        Track.builder().question_id("62d8e2d5a49a42d5112f23c2").answer_id(Arrays.asList(1)).build(),  // 최근 시력이
                        Track.builder().question_id("62d8e2e4a49a42d5112f23c3").answer_id(Arrays.asList(1)).build(),  // 안면 마비나
                        Track.builder().question_id("62d8e2fba49a42d5112f23c4").answer_id(Arrays.asList(1)).build())) // 이명이나
                .build();

        // when
        DiagnosisResponseDto diagnosis = questionService.findHeadacheDecisiveQuestion(dto);

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
//    @DisplayName("두통 초기 질문")
//    @Test
//    void getHeadacheFirstQuestion(){
//        // when
//        QuestionDto first_question = questionService.findFirstQuestion("headache");
//
//        // then
//        assertThat(first_question.getQuestion()).isEqualTo("언제부터 통증이 시작되었나요?");
//    }

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

    // 수면장애 수면위생점수 SHI 계산
    @DisplayName("수면장애 수면위생점수 SHI 계산")
    @Test
    void testGetSHI(){
        // given
        List<Track> tracks = new ArrayList<Track>();

        tracks.add(Track.builder().question_id("631ad4c675ce6608eb91f75a").answer_id(Arrays.asList(1)).build()); // 1점
        tracks.add(Track.builder().question_id("631ad45475ce6608eb91f759").answer_id(Arrays.asList(1)).build()); // 1점
        tracks.add(Track.builder().question_id("631ad41075ce6608eb91f757").answer_id(Arrays.asList(0,1,2,3)).build()); // 4점

        // then
        assertThat(questionService.getSHI(tracks)).isEqualTo(6);
    }

    // 수면장애 결정적 질문 진단결과 조회 및 로그 저장
    @DisplayName("수면장애 결정적 질문 응답")
    @Test
    void testFindDecisiveQuestion(){
        // given
        DecisiveQuestionRequestDto dto = DecisiveQuestionRequestDto.builder()
                .questionId("631ad32875ce6608eb91f755")
                .answerId(1)
                .gender("f")
                .birthYear(2000)
                .interests(Arrays.asList(0))
                .tracks(Arrays.asList(
                        Track.builder().question_id("631ad53675ce6608eb91f75c").answer_id(Arrays.asList(1)).build(),
                        Track.builder().question_id("631ad4f175ce6608eb91f75b").answer_id(Arrays.asList(1)).build(),
                        Track.builder().question_id("631ad4c675ce6608eb91f75a").answer_id(Arrays.asList(1)).build(),
                        Track.builder().question_id("631ad45475ce6608eb91f759").answer_id(Arrays.asList(1)).build(),
                        Track.builder().question_id("631ad41075ce6608eb91f757").answer_id(Arrays.asList(1)).build(), // 5,6,7 로 바꾸면 "수면습관 주의"
                        Track.builder().question_id("631ad32875ce6608eb91f755").answer_id(Arrays.asList(1)).build()
                )).build();

        // when
        DiagnosisResponseDto diagnosisResponseDto = questionService.findDecisiveQuestion(dto);

        // then
        assertThat(diagnosisResponseDto.getDiagnosticResult().getTitle()).isEqualTo("수면장애가 아니에요");
    }

}