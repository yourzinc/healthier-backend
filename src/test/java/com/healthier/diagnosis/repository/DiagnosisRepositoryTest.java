package com.healthier.diagnosis.repository;

import com.healthier.diagnosis.domain.diagnosis.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DiagnosisRepositoryTest {

    @Autowired
    private DiagnosisRepository diagnosisRepository;

    @DisplayName("진단 결과 조회")
    @Test
    void findById(){
        //given
        String id = "62c795799d8ed7017f145df8";

        //when
        Diagnosis diagnosis = diagnosisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("이런 진단명은 없습니다 환자분.."));

        //then
        assertThat(diagnosis.getSeverity()).isEqualTo(2);
        assertThat(diagnosis.getH1()).isEqualTo("나도 모르는 새에 자꾸자꾸 깨는 당신은");
    }

}