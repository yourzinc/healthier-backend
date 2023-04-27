package com.healthier.diagnosis.dto.question;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class QuestionDto {
    private String id;
    private String question;

    private int isMultiple;
    private ArrayList<AnswerDto> answers;

    private int isLastDefault;
}