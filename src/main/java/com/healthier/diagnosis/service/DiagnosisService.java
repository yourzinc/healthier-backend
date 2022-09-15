package com.healthier.diagnosis.service;

import com.healthier.diagnosis.domain.diagnosis.Diagnosis;
import com.healthier.diagnosis.dto.DiagnosisResponseDto;
import com.healthier.diagnosis.exception.CustomException;
import com.healthier.diagnosis.exception.ErrorCode;
import com.healthier.diagnosis.repository.DiagnosisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DiagnosisService {
    private final DiagnosisRepository diagnosisRepository;

    /**
     * 진단결과 조회
     */
    public DiagnosisResponseDto findDiagnosis(String id) {
        Diagnosis diagnosis = diagnosisRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.DIAGNOSIS_NOT_FOUND));

        return DiagnosisResponseDto.builder()
                .isResult(1)
                .diagnosticResult(diagnosis)
                .build();
    }

    /**
     * 수면장애 : 불면증에서 기간에 따른 진단
     */
    public String findInsomniaPeriod(String id, int period) {
        Diagnosis diagnosis = diagnosisRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.DIAGNOSIS_NOT_FOUND));

        String result_diagnosis_id;

        if (period == 1) { // temporary
            result_diagnosis_id = diagnosisRepository.findById(diagnosis.getTemporary_diagnosis())
                    .orElseThrow(() -> new CustomException(ErrorCode.DIAGNOSIS_NOT_FOUND)).getId();
        } else if (period > 1 && period < 3) { // short
            result_diagnosis_id = diagnosisRepository.findById(diagnosis.getShort_diagnosis())
                    .orElseThrow(() -> new CustomException(ErrorCode.DIAGNOSIS_NOT_FOUND)).getId();

        } else { // long
            result_diagnosis_id = diagnosisRepository.findById(diagnosis.getLong_diagnosis())
                    .orElseThrow(() -> new CustomException(ErrorCode.DIAGNOSIS_NOT_FOUND)).getId();
        }

        return result_diagnosis_id;
    }

    /**
     * 두통 : 약물과용 두통, 경미, 주의, 심각 두통 진단
     */
    public DiagnosisResponseDto checkMOH_mild_warning_severe(String id,
                                                             int is_taking_medicine,
                                                             int pain_level,
                                                             int period,
                                                             int cycle)
    {
        Diagnosis diagnosis = diagnosisRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.DIAGNOSIS_NOT_FOUND));

        Diagnosis resultDiagnosis = null;

        if (is_taking_medicine == 1) { // 약물과용 두통
            resultDiagnosis = diagnosisRepository.findById(diagnosis.getMOH())
                    .orElseThrow(() -> new CustomException(ErrorCode.DIAGNOSIS_NOT_FOUND));
        }
        else if ((period == 2) | (period == 3) & (cycle == 1)) { // 만성
            resultDiagnosis = diagnosisRepository.findById(diagnosis.getSevere_headache())
                    .orElseThrow(()-> new CustomException(ErrorCode.DIAGNOSIS_NOT_FOUND));
        }
        else if ((pain_level == 1 ) | (pain_level == 2)) { // 경미
            resultDiagnosis = diagnosisRepository.findById(diagnosis.getMild_headache())
                    .orElseThrow(()-> new CustomException(ErrorCode.DIAGNOSIS_NOT_FOUND));
        }
        else if ((pain_level == 3 ) | (pain_level == 4)) { // 주의
            resultDiagnosis = diagnosisRepository.findById(diagnosis.getWarning_headache())
                    .orElseThrow(()-> new CustomException(ErrorCode.DIAGNOSIS_NOT_FOUND));
        }

        return DiagnosisResponseDto.builder()
                .isResult(1)
                .diagnosticResult(resultDiagnosis)
                .build();
    }
}