package com.healthier.diagnosis.dto;

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
    private Diagnosis diagnostic_result;
}
