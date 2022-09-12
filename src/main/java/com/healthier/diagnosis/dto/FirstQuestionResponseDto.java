package com.healthier.diagnosis.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FirstQuestionResponseDto {

    private String id;
    private String question;

    private int isMultiple;
    private ArrayList<Answer> answers;

    private int isLastDefault;
}

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
class Answer {
    private int answer_id;
    private String answer;
    private int isDecisive;  // 1이면 진단 결과 도출
}