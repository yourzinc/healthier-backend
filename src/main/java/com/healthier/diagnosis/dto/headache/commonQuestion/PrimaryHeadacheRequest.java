package com.healthier.diagnosis.dto.headache.commonQuestion;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PrimaryHeadacheRequest {
    private List<QnA> questions;
    private int type;
    private String message;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class QnA {
        private int questionId;
        private int answerId;
    }
}