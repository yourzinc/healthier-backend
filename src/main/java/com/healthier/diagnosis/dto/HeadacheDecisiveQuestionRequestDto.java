package com.healthier.diagnosis.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

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
    private List<Integer> interests;
    private List<DecisiveQuestionRequestDto.Track> tracks;   // 질문 - 답변 추적

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Track {
        private String question_id;
        private List<Integer> answer_id;
    }

}
