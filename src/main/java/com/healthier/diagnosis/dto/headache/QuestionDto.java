package com.healthier.diagnosis.dto.headache;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.healthier.diagnosis.domain.headache.Answer;
import com.healthier.diagnosis.domain.headache.Question;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class QuestionDto {
    int id;
    String question;
    Boolean isMultiple;
    List<AnswerDto> answers = new ArrayList<>();

    public QuestionDto(Question question) {
        this.id = question.getId();
        this.question = question.getQuestion();

        if (question.getIsMultiple() == null) {
            this.isMultiple = Boolean.FALSE;
        }
        else {
            this.isMultiple = question.getIsMultiple();
        }

        for (Answer answer : question.getAnswers()) {
            answers.add(new AnswerDto(answer));
        }
    }
}