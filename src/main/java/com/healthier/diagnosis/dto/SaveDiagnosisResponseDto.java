package com.healthier.diagnosis.dto;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class SaveDiagnosisResponseDto {
    private List<MainDiagnosisDto> diagnosis;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MainDiagnosisDto {
        private String diagnosis_id;
        private String title;
        private int severity;
        private LocalDateTime is_created;
    }

}
