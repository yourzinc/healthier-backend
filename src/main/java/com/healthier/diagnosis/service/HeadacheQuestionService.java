package com.healthier.diagnosis.service;

import com.healthier.diagnosis.domain.headache.Answer;
import com.healthier.diagnosis.domain.headache.Question;
import com.healthier.diagnosis.dto.headache.ResultDto;
import com.healthier.diagnosis.dto.headache.painArea.HeadachePainAreaNextResponse;
import com.healthier.diagnosis.repository.HeadacheQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HeadacheQuestionService {
    private final HeadacheQuestionRepository questionRepository;

    /**
     * 특정 통증 부위 시작 질문 조회
     */
    public Optional<Question> findPainAreaFirstQuestion(String painSite) {
        return questionRepository.findByPainSiteContainsAndIsFirst(painSite, true);
    }

    /**
     * 특정 통증 부위 다음 질문 조회
     * type 1 : 다음 질문
     * type 2 : 진단 결과 안내
     */
    public HeadachePainAreaNextResponse findPainAreaNextQuestion(int questionId, int answerId) {
        Optional<Question> question = questionRepository.findById(questionId);
        Answer answer = question.get().getAnswers().get(answerId); //답변 정보

        //다음 질문이 존재할 때
        if (!answer.isDecisive()) {
            int nextQuestionId = answer.getNextQuestionId(); //다음 질문 id
            return new HeadachePainAreaNextResponse(questionRepository.findById(nextQuestionId).get());
        }

        //다음 질문이 존재하지 않을 때
        else {
            return new HeadachePainAreaNextResponse(answer.getResultId(), answer.getResult());
        }
    }

    /**
     * 추가적인 악화 요인 질문 조회
     */
    public Question findAdditionalFactorQuestion() {
        List<Question> questions = questionRepository.findByType("additional-factor");
        return questions.get(0);
    }

    /**
     * 추가적인 악화 요인 결과
     */
    public ResultDto getAdditionalFactorResult(int questionId, int [] answerId ) {
        List<Answer> answers = questionRepository.findById(questionId).get().getAnswers();

        for (int i : answerId) {
            if (answers.get(i).getResultId() != 0) {
                return new ResultDto(answers.get(i).getResultId(), answers.get(i).getResult());
            }
        }

        return null;
    }
}
