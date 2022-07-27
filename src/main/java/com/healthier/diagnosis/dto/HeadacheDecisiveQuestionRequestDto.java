package com.healthier.diagnosis.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class HeadacheDecisiveQuestionRequestDto {
    @NotNull
    private String questionId;
    @NotNull
    private int answerId;

    private int period; // 기간
    private int cycle;  // 주기
    private int pain_level; // 통증의 정도
    private int is_taking_medication; // 약물 지속적 복용

    private String gender;
    private int birthYear;
    private ArrayList<Integer> interests;
}
