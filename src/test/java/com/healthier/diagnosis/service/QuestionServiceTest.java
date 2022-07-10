package com.healthier.diagnosis.service;

import com.healthier.diagnosis.domain.question.Question;
import com.healthier.diagnosis.dto.FirstQuestionRequestDto;
import com.healthier.diagnosis.dto.QuestionRequestDto;
import com.healthier.diagnosis.dto.QuestionResponseDto;
import com.healthier.diagnosis.repository.QuestionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    @DisplayName("첫번째 질문 조회")
    @Test
    void findFirstQuestion() {
        //given
        String id = "62ca4918705b0e3bdeefc746";

        //when
        QuestionResponseDto question = questionService.findFirstQuestion();

        //then
        assertThat(question.getQuestion().getQuestion()).isEqualTo("자신을 가장 잘 설명하는 증상을 골라주세요");
    }
}