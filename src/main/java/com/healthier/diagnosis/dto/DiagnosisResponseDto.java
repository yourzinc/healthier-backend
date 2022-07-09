package com.healthier.diagnosis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.healthier.diagnosis.domain.diagnosis.Diagnosis;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DiagnosisResponseDto {
    @JsonProperty("is_result")
    private int isResult;

    @JsonProperty("diagnostic_result")
    private Diagnosis diagnosticResult;
}
