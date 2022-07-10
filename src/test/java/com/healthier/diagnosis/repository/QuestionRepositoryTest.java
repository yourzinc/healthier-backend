package com.healthier.diagnosis.repository;

import com.healthier.diagnosis.domain.diagnosis.Diagnosis;
import com.healthier.diagnosis.domain.question.Question;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @DisplayName("질문 조회")
    @Test
    void findById(){

        //given
        String id = "62ca494f705b0e3bdeefc747";

        //when
        Question in_question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("이런 질문은 없습니다 환자분.."));

        //then
        assertThat(in_question.getQuestion()).isEqualTo("다음 증상 중 자신을 가장 잘 설명하는 증상을 하나를 선택하세요.");
    }
}