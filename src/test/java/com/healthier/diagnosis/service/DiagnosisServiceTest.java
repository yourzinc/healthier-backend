package com.healthier.diagnosis.service;

import com.healthier.diagnosis.dto.DiagnosisResponseDto;
import com.healthier.diagnosis.domain.diagnosis.Diagnosis;
import com.healthier.diagnosis.repository.DiagnosisRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DiagnosisServiceTest {

    @Autowired
    DiagnosisRepository diagnosisRepository;

    @Autowired
    DiagnosisService diagnosisService;

    @DisplayName("진단 결과 조회")
    @Test
    void findDiagnosis(){
        String id = "62c795799d8ed7017f145df8";

        DiagnosisResponseDto diagnosis = diagnosisService.findDiagnosis(id);

        assertThat(diagnosis.getDiagnostic_result().getSeverity()).isEqualTo(2);
        assertThat(diagnosis.getDiagnostic_result().getH1()).isEqualTo("나도 모르는 새에 자꾸자꾸 깨는 당신은");
    }
}