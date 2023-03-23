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
    private List<QnARequest> questions;
    private int type;
    private String message;
}