package com.healthier.diagnosis.service;

import com.healthier.diagnosis.dto.DiagnosisResponseDto;
import com.healthier.diagnosis.dto.headache.ResultDto;
import com.healthier.diagnosis.dto.headache.result.HeadacheResultResponse;
import com.healthier.diagnosis.dto.headache.result.ResultDetail;
import com.healthier.diagnosis.exception.CustomException;
import com.healthier.diagnosis.repository.DiagnosisRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @DisplayName("두통 최종 문진 결과 우선순위 조회 - type 1")
    @Test
    public void getHeadacheResultPriorityTest1() throws Exception {
        //given
        List<ResultDto> results = new ArrayList<>();

        results.add(new ResultDto(1002, "전조증상이 있는 편두통"));
        results.add(new ResultDto(1022, "대후두 신경통"));

        //when

        HeadacheResultResponse resultPriority = diagnosisService.getHeadacheResultPriority(results);

        //then
        Assertions.assertThat(resultPriority.getResults().getLikely().get(0).getContent()).isEqualTo("전조증상이 있는 편두통");
        Assertions.assertThat(resultPriority.getResults().getSuspicious().get(0).getContent()).isEqualTo("대후두 신경통");
        Assertions.assertThat(resultPriority.getResults().getPredicted()).isEqualTo(null);
    }

    @DisplayName("두통 최종 문진 결과 우선순위 조회 - type 3")
    @Test
    public void getHeadacheResultPriorityTest3() throws Exception {
        //given
        List<ResultDto> results = new ArrayList<>();

        //when

        HeadacheResultResponse resultPriority = diagnosisService.getHeadacheResultPriority(results);

        //thens
        Assertions.assertThat(resultPriority.getResults().getLikely().get(0).getContent()).isEqualTo("긴장성 두통");
        Assertions.assertThat(resultPriority.getResults().getPredicted()).isEqualTo(null);
    }

    @DisplayName("두통 최종 문진 결과 우선순위 조회 - type 4")
    @Test
    public void getHeadacheResultPriorityTest4() throws Exception {
        //given
        List<ResultDto> results = new ArrayList<>();
        results.add(new ResultDto(1022, "대후두 신경통"));
        results.add(new ResultDto(1025, "약물 과용으로 인한 두통"));

        //when

        HeadacheResultResponse resultPriority = diagnosisService.getHeadacheResultPriority(results);

        //thens
        Assertions.assertThat(resultPriority.getResults().getSuspicious().get(0).getContent()).isEqualTo("대후두 신경통");
        Assertions.assertThat(resultPriority.getResults().getPredicted().get(0).getContent()).isEqualTo("약물 과용으로 인한 두통");
    }

    @DisplayName("두통 진단 결과 상세 조회- O")
    @Test
    public void getHeadacheResultDetailTest1() throws Exception {
        //given
        int resultId = 1025;

        //when
        ResultDetail headacheResultDetail = diagnosisService.getHeadacheResultDetail(resultId);

        //then
        Assertions.assertThat(headacheResultDetail.getDiagnosticResult().getId()).isEqualTo(1025);
    }

    @DisplayName("두통 진단 결과 상세 조회- X 존재하지 않는 경우")
    @Test
    public void getHeadacheResultDetailTest2() throws Exception {
        //given
        int resultId = 2025;

        //when
        Throwable exception = assertThrows(CustomException.class, () -> diagnosisService.getHeadacheResultDetail(resultId));

        assertEquals(exception.getMessage(), "해당 진단 정보를 찾을 수 없습니다.");
    }
}