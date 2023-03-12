package com.healthier.diagnosis.dto.headache;

import com.healthier.diagnosis.domain.headache.Answer;
import lombok.Data;

@Data
class AnswerDto {
    int answer_id;
    String answer;

    AnswerDto(Answer answer) {
        this.answer_id = answer.getAnswerId();
        this.answer = answer.getAnswer();
    }
}
