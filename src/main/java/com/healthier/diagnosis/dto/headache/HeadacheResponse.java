package com.healthier.diagnosis.dto.headache;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class HeadacheResponse {
    private int type;
    private String message;
    private List<QuestionDto> questions;
    private ResultDto result;
}
