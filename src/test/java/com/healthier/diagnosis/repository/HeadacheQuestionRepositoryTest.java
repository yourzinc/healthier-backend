package com.healthier.diagnosis.repository;

import com.healthier.diagnosis.domain.headache.Question;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HeadacheQuestionRepositoryTest {

    @Autowired
    private HeadacheQuestionRepository headacheQuestionRepository;

    @DisplayName("id 로 질문 조회")
    @Test
    void findById() {
        //given
        int id = 100;

        //when
        Optional<Question> question = headacheQuestionRepository.findById(id);

        //then
        assertThat(question.get().getQuestion())
                .isEqualTo("언제부터 통증이 시작되었나요?\\n(처음으로 이 증상을 겪기 시작한 때부터 현재까지의 기간)");

        assertThat(question.get().getAnswers().get(0).getAnswer())
                .isEqualTo("3개월 이상");
    }

    @DisplayName("Type 으로 질문 조회")
    @Test
    void findByType() {
        //given
        String type = "basic";

        //when
        List<Question> questions = headacheQuestionRepository.findByType(type);

        //then
        assertThat(questions.get(0).getQuestion())
                .isEqualTo("언제부터 통증이 시작되었나요?\\n(처음으로 이 증상을 겪기 시작한 때부터 현재까지의 기간)");

        assertThat(questions.get(0).getAnswers().get(0).getAnswer())
                .isEqualTo("3개월 이상");
    }

    @DisplayName("통증 부위 시작 질문 조회")
    @Test
    void findByPainSite() {
        //given
        String painSite1 = "뒷머리";
        String painSite2 = "뒷목";

        //when
        Optional<Question> question1 = headacheQuestionRepository.findByPainSiteContainsAndIsFirst(painSite1, true);
        Optional<Question> question2 = headacheQuestionRepository.findByPainSiteContainsAndIsFirst(painSite2, true);

        //then
        assertThat(question1.get().getQuestion())
                .isEqualTo("목을 움직이거나 손으로 목근육을 눌렀을 때 평소 느끼는 통증과 유사한가요?");

        assertThat(question2.get().getQuestion())
                .isEqualTo(question1.get().getQuestion());
    }
}