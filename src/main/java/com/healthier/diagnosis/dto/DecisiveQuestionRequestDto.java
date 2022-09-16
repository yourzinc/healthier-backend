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
    private String gender;
    private int birthYear;
    private List<Integer> interests;
    private List<Track> tracks;   // 질문 - 답변 추적
}
