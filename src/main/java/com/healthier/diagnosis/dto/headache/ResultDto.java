package com.healthier.diagnosis.dto.headache;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResultDto {
    private int resultId;
    private String result;

    protected ResultDto() {}

    public ResultDto(int resultId, String result) {
        this.resultId = resultId;
        this.result = result;
    }
}

