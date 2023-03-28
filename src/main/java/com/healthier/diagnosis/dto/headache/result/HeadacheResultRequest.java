package com.healthier.diagnosis.dto.headache.result;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.healthier.diagnosis.dto.headache.ResultDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class HeadacheResultRequest {

    private String gender;
    private int birthYear;
    private List<Integer> interests;
    private List<Track> tracks;         // 질문/답변
    private List<ResultDto> results;    // 문진결과

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Track {
        private int questionId;
        private List<Integer> answerId;
    }
}
