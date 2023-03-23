package com.healthier.diagnosis.service;

import com.healthier.diagnosis.domain.headache.Question;
import com.healthier.diagnosis.dto.headache.ResultDto;
import com.healthier.diagnosis.dto.headache.painArea.HeadachePainAreaNextResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class HeadacheQuestionServiceTest {
    @Autowired
    private HeadacheQuestionService questionService;

    @DisplayName("특정 통증 부위 시작 질문 - 코, 코 주위")
    @Test
    public void findPainAreaFirstNose() throws Exception {
        //given
        String painArea1 = "코";
        String painArea2 = "코 주위";

        //when
        Optional<Question> question1 = questionService.findPainAreaFirstQuestion(painArea1);
        Optional<Question> question2 = questionService.findPainAreaFirstQuestion(painArea2);

        //then
        Assertions.assertThat(question1.get().getQuestion()).isEqualTo("머리를 숙이거나 흔들면 두통이 심화되나요?");
        Assertions.assertThat(question1.get().getQuestion()).isEqualTo(question2.get().getQuestion());
    }

    @DisplayName("특정 통증 부위 시작 질문 - 뒷머리, 뒷목")
    @Test
    public void findPainAreaFirstBack() throws Exception {
        //given
        String painArea1 = "뒷머리";
        String painArea2 = "뒷목";

        //when
        Optional<Question> question1 = questionService.findPainAreaFirstQuestion(painArea1);
        Optional<Question> question2 = questionService.findPainAreaFirstQuestion(painArea2);

        //then
        Assertions.assertThat(question1.get().getQuestion()).isEqualTo("목을 움직이거나 손으로 목근육을 눌렀을 때 평소 느끼는 통증과 유사한가요?");
        Assertions.assertThat(question1.get().getQuestion()).isEqualTo(question2.get().getQuestion());
    }

    @DisplayName("특정 통증 부위 다음 질문 - Type 1 : 다음 질문")
    @Test
    public void findPainAreaNextQuestion() throws Exception {
        //given
        int questionId = 461;
        int answerId = 1;

        //when
        HeadachePainAreaNextResponse next = questionService.findPainAreaNextQuestion(questionId, answerId);

        //then
        Assertions.assertThat(next.getType()).isEqualTo(1);
        Assertions.assertThat(next.getQuestions().get(0).getQuestion()).isEqualTo("얼굴 피부 표면에 외상 또는 찰과상이 있나요?");
    }

    @DisplayName("특정 통증 부위 다음 질문 - Type 2: 진단 결과")
    @Test
    public void findPainAreaNextQuestionResult() throws Exception {
        //given
        int questionId = 461;
        int answerId = 0;

        //when
        HeadachePainAreaNextResponse next = questionService.findPainAreaNextQuestion(questionId, answerId);

        //then
        Assertions.assertThat(next.getType()).isEqualTo(2);
        Assertions.assertThat(next.getResult().getResult()).isEqualTo("삼차신경통");
    }

    @DisplayName("추가적인 악화 요인 질문")
    @Test
    public void findAdditionalFactorQuestion() throws Exception {
        //given
        Question question = questionService.findAdditionalFactorQuestion();

        //then
        Assertions.assertThat(question.getQuestion()).isEqualTo("추가적인 악화요인을 살펴볼게요!\\n다음 중 해당되는 것을 모두 골라주세요.");
        Assertions.assertThat(question.getIsMultiple()).isEqualTo(true);
    }

    @DisplayName("추가적인 악화 요인 결과")
    @Test
    public void getAdditionalFactorResultTest() throws Exception {
        //given
        int  questionId = 601;
        int [] answerId1 = {2,3};
        int [] answerId2 = {1};

        // when
        ResultDto resultDto1 = questionService.getAdditionalFactorResult(questionId, answerId1);
        ResultDto resultDto2 = questionService.getAdditionalFactorResult(questionId, answerId2);

        //then
        Assertions.assertThat(resultDto1.getResult()).isEqualTo("약물 과용으로 인한 두통");
        Assertions.assertThat(resultDto2).isEqualTo(null);
    }
}