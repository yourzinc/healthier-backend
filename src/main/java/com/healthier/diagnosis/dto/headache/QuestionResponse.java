package com.healthier.diagnosis.dto.headache;

import com.healthier.diagnosis.domain.headache.Question;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuestionResponse {
    private List<QuestionDto> questions = new ArrayList<>();


    protected QuestionResponse() { }

    public QuestionResponse(Question question) {
        questions.add(new QuestionDto(question));
    }
}
