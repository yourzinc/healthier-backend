package com.healthier.diagnosis.service;

import com.healthier.diagnosis.domain.question.Answer;
import com.healthier.diagnosis.domain.question.Question;
import com.healthier.diagnosis.dto.FirstQuestionRequestDto;
import com.healthier.diagnosis.dto.QuestionRequestDto;
import com.healthier.diagnosis.dto.QuestionResponseDto;
import com.healthier.diagnosis.exception.CustomException;
import com.healthier.diagnosis.exception.ErrorCode;
import com.healthier.diagnosis.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

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
        Question out_question = questionRepository.findById(in_answer.getNext_question_id())
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

        return QuestionResponseDto.builder()
                .isResult(0)
                .question(out_question)
                .build();
    }

    /**
     * 첫번째 질문 조회
     */
    public QuestionResponseDto findFirstQuestion() {

        // 첫번째 질문 정보 조회
        Question question = questionRepository.findById(ID)
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

        return QuestionResponseDto.builder()
                .isResult(0)
                .question(question)
                .build();
    }
}
