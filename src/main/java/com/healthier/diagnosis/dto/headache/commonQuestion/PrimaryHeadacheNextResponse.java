package com.healthier.diagnosis.dto.headache.commonQuestion;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.healthier.diagnosis.dto.headache.QuestionDto;
import lombok.*;

import java.util.List;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PrimaryHeadacheNextResponse {
    private int type;
    private Result result;
    private List<QuestionDto> questions;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Result {
        private int id;
        private String content;
    }
}
