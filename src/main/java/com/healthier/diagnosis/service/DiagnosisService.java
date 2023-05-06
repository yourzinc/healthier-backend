package com.healthier.diagnosis.service;

import com.healthier.diagnosis.domain.diagnosis.Diagnosis;
import com.healthier.diagnosis.domain.log.Log;
import com.healthier.diagnosis.domain.user.Track;
import com.healthier.diagnosis.dto.question.DiagnosisResponseDto;
import com.healthier.diagnosis.dto.headache.ResultDto;
import com.healthier.diagnosis.dto.headache.result.*;
import com.healthier.diagnosis.exception.CustomException;
import com.healthier.diagnosis.exception.ErrorCode;
import com.healthier.diagnosis.repository.DiagnosisLogRepository;
import com.healthier.diagnosis.repository.DiagnosisRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DiagnosisService {
    private final DiagnosisRepository diagnosisRepository;

    private final DiagnosisLogRepository logRepository;

    ModelMapper modelMapper = new ModelMapper();

    private static final Boolean LOG_FLAG = false;

    private static final int MEDICATION_OVER_USE_ID = 1025;
    private static final int TENSION_HEADACHE_ID = 1015;
    private static final int[] PRIMARY_HEADACHE_ID = new int[]{1002, 1003, 1015, 1019};


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
     * 수면장애 : 수면장애가 아닌 경우 수면위생점수로 진단
     */
    public String findSleepdisorderWithSHI(String id, int SHI) {
        Diagnosis diagnosis = diagnosisRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.DIAGNOSIS_NOT_FOUND));

        String result_diagnosis_id;

        if (SHI < 0 || SHI > 17) {
            throw new CustomException(ErrorCode.RANGE_NOT_SATISFIABLE);
        }
        if (SHI <= 6) { // 수면 장애 아님
            result_diagnosis_id = diagnosis.getNotSleepdisorder();
        } else if (SHI >= 11) { // 수면습관 경고
            result_diagnosis_id = diagnosis.getSleepWarning();
        } else { // 수면습관 주의
            result_diagnosis_id = diagnosis.getSleepCaution();
        }

        return result_diagnosis_id;
    }

    /**
     * 수면장애 : 불면증에서 기간에 따른 진단
     */
    public String findInsomniaPeriod(String id, int period) {
        Diagnosis diagnosis = diagnosisRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.DIAGNOSIS_NOT_FOUND));

        String result_diagnosis_id;

        if (period == 1) { // temporary
            result_diagnosis_id = diagnosisRepository.findById(diagnosis.getTemporaryDiagnosis())
                    .orElseThrow(() -> new CustomException(ErrorCode.DIAGNOSIS_NOT_FOUND)).getId();
        } else if (period > 1 && period < 3) { // short
            result_diagnosis_id = diagnosisRepository.findById(diagnosis.getShortDiagnosis())
                    .orElseThrow(() -> new CustomException(ErrorCode.DIAGNOSIS_NOT_FOUND)).getId();

        } else { // long
            result_diagnosis_id = diagnosisRepository.findById(diagnosis.getLongDiagnosis())
                    .orElseThrow(() -> new CustomException(ErrorCode.DIAGNOSIS_NOT_FOUND)).getId();
        }

        return result_diagnosis_id;
    }

    /**
     * 두통 : 약물과용 두통, 경미, 주의, 심각 두통 진단
     */
    public String checkMOH_mild_warning_severe(String id,
                                               int is_taking_medicine,
                                               int pain_level,
                                               int period,
                                               int cycle) {
        Diagnosis diagnosis = diagnosisRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.DIAGNOSIS_NOT_FOUND));

        if (is_taking_medicine == 0) { // 약물과용 두통
            return diagnosis.getMOH();
        } else if (((period == 2) | (period == 3)) & (cycle == 0)) { // 만성
            return diagnosis.getSevereHeadache();
        } else if ((pain_level == 1) | (pain_level == 2)) { // 경미
            return diagnosis.getMildHeadache();
        } else { // 주의
            return diagnosis.getWarningHeadache();
        }

    }

    /**
     * 두통 : 최종 문진 결과
     */
    public HeadacheResultResponse getHeadacheResult(HeadacheResultRequest request) {


        /**
         * TODO: Headache log : 기존의 Log 와 차이점
         * 1. 진단 결과가 여러개 도출
         * 2. category = headache 추가 예정 ( 후에 data 분석을 위해서 ! )
         */

        // 전체 Log 남기기
        if (LOG_FLAG) {
            logRepository.save(
                    Log.builder()
                            .gender(request.getGender())
                            .is_created(LocalDateTime.now())
                            .birthyear(request.getBirthYear())
                            .interests(request.getInterests())
                            .tracks(request.getTracks()
                                    .stream()
                                    .map(c -> modelMapper.map(c, Track.class))
                                    .collect(Collectors.toList()))
                            .build()
            );
        }

        // 우선순위 정렬
        List<ResultDto> results = request.getResults();

        return getHeadacheResultPriority(results);
    }

    /**
     * 두통 : 최종 문진 결과 우선순위
     *
     * 1) 일차성두통 O and 눈/뒷목/뒷머리/턱/코주위/얼굴피부 결과 O
     * likely. 가능성 높은 질환이에요 (일차성 두통)
     * suspicious. 다음질환도 의심되어요 (부위별 진단 + 약물 과용으로 인한 두통)
     *
     * 2) 일차성두통 O and 눈/뒷목/뒷머리/턱/코주위/얼굴피부 결과 X
     * likely. 가장 가능성 높은 질환이에요 (일차성 두통)
     * suspicious. 다음질환도 의심되어요 (부위별 진단 + 약물 과용으로 인한 두통)
     *
     * 3) 일차성두통 X and 눈/뒷목/뒷머리/턱/코주위/얼굴피부 결과 X
     * likely. 가능성이 높은 질환이에요 (긴장성 두통)
     * suspicious. 다음질환도 의심되어요 (+ 약물 과용으로 인한 두통)
     *
     * 4) 일차성두통 X and 눈/뒷목/뒷머리/턱/코주위/얼굴피부 결과 O
     * predicted. 예상질환이에요 (부위별 진단)
     * suspicious. 다음질환도 의심되어요 (+ 약물 과용으로 인한 두통)
     *
     */

    public HeadacheResultResponse getHeadacheResultPriority(List<ResultDto> resultList) {

        List<HeadacheResultDto> likely = new ArrayList<>(); // 가능성이 높은 질환이에요
        List<HeadacheResultDto> predicted = new ArrayList<>(); // 예상질환이에요
        List<HeadacheResultDto> suspicious = new ArrayList<>(); // 다음질환도 의심되어요



        boolean flag1 = false; // 일차성 두통 존재 flag
        boolean flag2 = false; // 일차성 두통 존재 flag

        for (ResultDto result : resultList) {
            Diagnosis diagnosis = diagnosisRepository.findByNewId(result.getResultId()).get();
            HeadacheResultDto resultDto = new HeadacheResultDto(result.getResultId(), result.getResult(), diagnosis.getBannerIllustration());

            if (!flag2) {
                for (int id : PRIMARY_HEADACHE_ID) {
                    if (result.getResultId() == id) {
                        flag1 = true;
                        flag2 = true;
                        break;
                    }
                }
            }

            if (flag1) {
                likely.add(resultDto); // likely
                flag1 = false;
            }
            else if (result.getResultId() == MEDICATION_OVER_USE_ID) {
                // 약물 과용으로 인한 두통
                predicted.add(resultDto);
            } else {
                // 부위별 결과
                suspicious.add(resultDto);
            }
        }

        if (suspicious.size() == 0) { suspicious = null; }
        if (predicted.size() == 0) { predicted = null; }

        if (flag2) {
            // 1) 일차성두통 O and 눈/뒷목/뒷머리/턱/코주위/얼굴피부 결과 O
            // 2) 일차성두통 O and 눈/뒷목/뒷머리/턱/코주위/얼굴피부 결과 X

            return new HeadacheResultResponse(1, new HeadacheResult(likely, suspicious, predicted));

        } else if (suspicious == null) {
            // 3) 일차성두통 X and 눈/뒷목/뒷머리/턱/코주위/얼굴피부 결과 X

            Diagnosis diagnosis = diagnosisRepository.findByNewId(TENSION_HEADACHE_ID).get(); // likely = 긴장성 두통
            HeadacheResultDto resultDto = new HeadacheResultDto(diagnosis.getNewId(), diagnosis.getTitle(), diagnosis.getBannerIllustration());
            likely.add(resultDto);

            return new HeadacheResultResponse(3, new HeadacheResult(likely, null, predicted));

        } else {
            // 4) 일차성두통 X and 눈/뒷목/뒷머리/턱/코주위/얼굴피부 결과 O

            return new HeadacheResultResponse(4, new HeadacheResult(null, suspicious, predicted));
        }
    }


    /**
     * 두통 진단 결과 상세 조회
     */
    public ResultDetail getHeadacheResultDetail(int resultId) {
        Diagnosis diagnosis = diagnosisRepository.findByNewId(resultId)
                .orElseThrow(()-> new CustomException(ErrorCode.DIAGNOSIS_NOT_FOUND));

        return ResultDetail.builder()
                .isResult(1)
                .diagnosticResult(new ResultDetailDto(diagnosis))
                .build();
    }
}
