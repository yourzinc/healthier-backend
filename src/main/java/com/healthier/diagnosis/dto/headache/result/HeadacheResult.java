package com.healthier.diagnosis.dto.headache.result;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class HeadacheResult {
    private List<HeadacheResultDto> likely;
    private List<HeadacheResultDto> suspicious;
    private List<HeadacheResultDto> predicted;

    protected HeadacheResult() {}

    public HeadacheResult(List<HeadacheResultDto> likely, List<HeadacheResultDto> suspicious, List<HeadacheResultDto> predicted) {
        this.likely = likely;
        this.suspicious = suspicious;
        this.predicted  = predicted;
    }
}