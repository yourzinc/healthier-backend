package com.healthier.diagnosis.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.healthier.diagnosis.domain.user.Track;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DecisiveQuestionRequestDto {
    @NotNull
    private String questionId;
    @NotNull
    private int answerId;

    private int period;
    private int scoreB;  // 수면 위생 점수
    private String gender;
    private int birthYear;
    private List<Integer> interests;
    private List<Track> tracks;   // 질문 - 답변 추적

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Track {
        private String question_id;
        private List<Integer> answer_id;
    }
}
