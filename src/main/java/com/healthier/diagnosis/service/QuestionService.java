package com.healthier.diagnosis.service;

import com.healthier.diagnosis.domain.question.Answer;
import com.healthier.diagnosis.domain.question.Question;
import com.healthier.diagnosis.dto.DecisiveQuestionRequestDto;
import com.healthier.diagnosis.dto.FirstQuestionRequestDto;
import com.healthier.diagnosis.dto.QuestionRequestDto;
import com.healthier.diagnosis.dto.QuestionResponseDto;
import com.healthier.diagnosis.exception.CustomException;
import com.healthier.diagnosis.exception.ErrorCode;
import com.healthier.diagnosis.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class QuestionService {

    /**
     * TODO : 진단 결과 나올 때마다 정보 로그파일에 남기기
     */

    private final QuestionRepository questionRepository;
    private final DiagnosisService diagnosisService;

    private static final String ID = "62ca4918705b0e3bdeefc746"; // 첫번째 질문 id

    /**
     * 다음 질문 조회
     */
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
     * 첫번째 질문 조회
     */
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
     * 결정적 질문 진단결과 조회
     */
//    public Object findDecisiveQuestion(DecisiveQuestionRequestDto dto) {
//        Question in_question = questionRepository.findById(dto.getQuestionId())
//                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));
//
//        Answer in_answer = in_question.getAnswers().stream()
//                .filter(i -> i.getAnswer_id() == dto.getAnswerId())
//                .findFirst()
//                .orElseThrow(() -> new CustomException(ErrorCode.ANSWER_NOT_FOUND));
//
//    }

    /**
     * 다음 질문 반환 메소드
     */
    private QuestionResponseDto getQuestionResponseDto(String id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

        return QuestionResponseDto.builder()
                .isResult(0)
                .question(question)
                .build();
    }

    public Object findDecisiveQuestion(DecisiveQuestionRequestDto dto) {
        return new Object();
    }
}
