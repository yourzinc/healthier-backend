package com.healthier.diagnosis.dto.headache.commonQuestion;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.healthier.diagnosis.dto.headache.QuestionDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RedFlagSignResponse {
    private int type;
    private String message;
    private List<QuestionDto> questions;
}
