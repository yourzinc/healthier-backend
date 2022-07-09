package com.healthier.diagnosis.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.healthier.diagnosis.domain.question.Question;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class QuestionResponseDto {
    private int isResult; // 진단 결과이면 1
    private Question question;
}
