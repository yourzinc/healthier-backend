package com.healthier.diagnosis.service;

import com.healthier.diagnosis.domain.headache.Question;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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
}