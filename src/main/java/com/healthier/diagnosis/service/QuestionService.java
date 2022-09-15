package com.healthier.diagnosis.service;

import com.healthier.diagnosis.domain.question.Answer;
import com.healthier.diagnosis.domain.question.Question;
import com.healthier.diagnosis.domain.user.Log;
import com.healthier.diagnosis.domain.user.Track;
import com.healthier.diagnosis.dto.*;
import com.healthier.diagnosis.exception.CustomException;
import com.healthier.diagnosis.exception.ErrorCode;
import com.healthier.diagnosis.repository.DiagnosisLogRepository;
import com.healthier.diagnosis.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final DiagnosisLogRepository logRepository;
    private final DiagnosisService diagnosisService;
    ModelMapper modelMapper = new ModelMapper();

    private static final String ID = "62ca4918705b0e3bdeefc746"; // 첫번째 질문 id

    /**

     [수면장애]

     1. findFirstQuestion : 첫번째 질문 조회
     2. findDecisiveQuestion : 결정적 질문 진단결과 조회 및 로그 저장

     */


    // 수면장애/두통 초기 질문 조회
    public QuestionDto findFirstQuestion(String type)
    {
        Question first_question = questionRepository.findOneByIsDefaultAndType(1, type).orElseThrow(IllegalArgumentException::new);
        return modelMapper.map(first_question, QuestionDto.class);
    }


    /**
     * 첫번째 질문 조회
     */
    @Transactional(readOnly = true)
    public Object findFirstQuestion(FirstQuestionRequestDto dto) {
        if(dto.getAnswer().equals("n")){
            int score = dto.getScoreB();
            if(score < 0 || score > 17){
                throw new CustomException(ErrorCode.RANGE_NOT_SATISFIABLE);
            }
            if (score <= 6){ // 수면 장애 아님
                return diagnosisService.findDiagnosis("62ce908856e36933184b0fbd");
            }
            else if(score >= 11) { // 수면습관 경고
                return diagnosisService.findDiagnosis("62ce900456e36933184b0fba");
           }
            else { // 수면습관 주의
                return diagnosisService.findDiagnosis("62ce906a56e36933184b0fbc");
            }
        }

        // 첫번째 질문 정보 조회
        return getQuestionResponseDto(ID);
    }

    /**
     * 결정적 질문 진단결과 조회 및 로그 저장
     */
    public DiagnosisResponseDto findDecisiveQuestion(DecisiveQuestionRequestDto dto) {
        Question in_question = questionRepository.findById(dto.getQuestionId())
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

        Answer in_answer = in_question.getAnswers().stream()
                .filter(i -> i.getAnswer_id() == dto.getAnswerId())
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.ANSWER_NOT_FOUND));

        String resultId = in_answer.getResult_id();
        int period = dto.getPeriod();

        if (dto.getTracks() != null) {
            // 진단 로그 활성화
            logRepository.save(
                    Log.builder()
                            .diagnosis_id(resultId)
                            .gender(dto.getGender())
                            .is_created(LocalDateTime.now())
                            .birthyear(dto.getBirthYear())
                            .interests(dto.getInterests())
                            .tracks(dto.getTracks()
                                    .stream()
                                    .map(c -> modelMapper.map(c, Track.class))
                                    .collect(Collectors.toList()))
                            .build()
            );
        }

        // 심리적 불면증 or 수면환경 불면증 -> 기간 참조
        if (resultId.equals("62d17692f68f2b673e721211") || resultId.equals("62d176ecf68f2b673e721212")) {
            return diagnosisService.findPeriod(resultId, period);
        }
        else {
            return diagnosisService.findDiagnosis(resultId);
        }
    }


    /**

     [두통]

     1. findHeadacheDefaultQuestionAfter :  초기 질문 이후 이어지는 질문 조회
     2. findHeadacheDecisiveQuestion : 결정적 질문 진단결과 조회 및 로그 저장

     */

    public QuestionResponseDto findHeadacheDefaultQuestionAfter(HeadacheDefaultQuestionAfterRequestDto dto) {
        Question question = questionRepository.findBySiteId(dto.getSiteId())
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

        modelMapper.typeMap(Question.class, QuestionDto.class).addMappings(mapper -> {
           mapper.map(Question::getAnswers, QuestionDto::setAnswers);
        });

        return QuestionResponseDto.builder()
                .isResult(0)
                .question(modelMapper.map(question, QuestionDto.class))
                .build();
    }

    public DiagnosisResponseDto findHeadacheDecisiveQuestion(HeadacheDecisiveQuestionRequestDto dto) {
        Question in_question = questionRepository.findById(dto.getQuestionId())
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

        Answer in_answer = in_question.getAnswers().stream()
                .filter(i -> i.getAnswer_id() == dto.getAnswerId())
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.ANSWER_NOT_FOUND));

        String resultId = in_answer.getResult_id();

        if (dto.getTracks() != null) {
            // 진단 로그 활성화
            logRepository.save(
                    Log.builder()
                            .diagnosis_id(resultId)
                            .gender(dto.getGender())
                            .is_created(LocalDateTime.now())
                            .birthyear(dto.getBirthYear())
                            .interests(dto.getInterests())
                            .tracks(dto.getTracks()
                                    .stream()
                                    .map(c -> modelMapper.map(c, Track.class))
                                    .collect(Collectors.toList()))
                            .build()
            );
        }

        // 약물과용 두통 확인 -> is_taking_medicine 확인
        if (resultId.equals("62e11e121549f1a6fe9f58b0")){
            return diagnosisService.checkMOH_mild_warning_severe
                    (resultId, dto.getIs_taking_medication(), dto.getPain_level(), dto.getPeriod(), dto.getCycle());
        }

        return diagnosisService.findDiagnosis(resultId);
    }


    /**

     [공통]

     1. findNextQuestion : 다음 질문 조회
     2. getQuestionResponseDto : 다음 질문 반환 메소드

     */


    /**
     * 다음 질문 조회
     */
    @Transactional(readOnly = true)
    public QuestionResponseDto findNextQuestion(QuestionRequestDto dto) {
        // Request 질문-응답 정보 조회
        Question in_question = questionRepository.findById(dto.getQuestionId())
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

        Answer in_answer = in_question.getAnswers().stream()
                .filter(i -> i.getAnswer_id() == dto.getAnswerId())
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.ANSWER_NOT_FOUND));

        // 다음 질문 정보 조회
        return getQuestionResponseDto(in_answer.getNext_question_id());
    }

    /**
     * 다음 질문 반환 메소드
     */
    private QuestionResponseDto getQuestionResponseDto(String id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

        return QuestionResponseDto.builder()
                .isResult(0)
                .question(modelMapper.map(question, QuestionDto.class))
                .build();
    }

}
