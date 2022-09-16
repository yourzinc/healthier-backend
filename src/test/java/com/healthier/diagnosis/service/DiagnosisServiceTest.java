package com.healthier.diagnosis.service;

import com.healthier.diagnosis.dto.DiagnosisResponseDto;
import com.healthier.diagnosis.repository.DiagnosisRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DiagnosisServiceTest {

    @Autowired
    DiagnosisRepository diagnosisRepository;

    @Autowired
    DiagnosisService diagnosisService;

    @DisplayName("진단 결과 조회")
    @Test
    void findDiagnosis(){
        //given
        String id = "62cd703fe49face142d9cffe";

        //when
        DiagnosisResponseDto diagnosis = diagnosisService.findDiagnosis(id);

        //then
        assertThat(diagnosis.getDiagnosticResult().getSeverity()).isEqualTo(3);
        assertThat(diagnosis.getDiagnosticResult().getH1()).isEqualTo("나도 모르는 새에 자꾸자꾸 깨는 당신은");
    }

    @DisplayName("기간에 따른 진단결과 조회")
    @Test
    void findPeriod() {
        //given
        String id = "62d17692f68f2b673e721211"; // 심리적 불면증
        // String id = "62d176ecf68f2b673e721212"; // 수면 환경 불면증

        int period = 2; // 1이면 일시적 불면증, 2이면 단기 불면증, 3이면 만성 불면증

        //when
        DiagnosisResponseDto diagnosis = diagnosisService.findDiagnosis(diagnosisService.findInsomniaPeriod(id, period));

        //then
        assertThat(diagnosis.getDiagnosticResult().getTitle()).isEqualTo("단기 불면증");
    }
}