package com.healthier.diagnosis.dto;

import com.healthier.diagnosis.domain.user.User;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class SaveDiagnosisResponseDto {
    private String nickname;
    private List<MainDiagnosisDto> diagnosis;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MainDiagnosisDto {
        private User.Record Record;
        private String banner_illustration;
    }

}
