package com.healthier.diagnosis.service;

import com.healthier.diagnosis.domain.headache.Answer;
import com.healthier.diagnosis.domain.headache.Question;
import com.healthier.diagnosis.domain.question.Type;
import com.healthier.diagnosis.dto.headache.QuestionDto;
import com.healthier.diagnosis.dto.headache.commonQuestion.RedFlagSignRequest;
import com.healthier.diagnosis.dto.headache.commonQuestion.RedFlagSignResponse;
import com.healthier.diagnosis.dto.headache.painArea.HeadachePainAreaNextResponse;
import com.healthier.diagnosis.repository.HeadacheQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HeadacheQuestionService {
    private final HeadacheQuestionRepository questionRepository;

    /**
     * 두통 기본 질문 조회
     */
    public List<Question> findBasicQuestion() {
        return questionRepository.findByType(Type.BASIC.label());
    }

    /**
     * 두통 Red Flag Sign 질문 조회
     */
    public List<Question> findRedFlagSignQuestion() {
        return questionRepository.findByType(Type.REDFLAGSIGN.label());
    }

    /**
     * 두통 Red Flag Sign 결과
     */
    public RedFlagSignResponse findRedFlagSignResult(RedFlagSignRequest request) {
        // Red Flag Sign 진단
        if (isRedFlagSign(request)) {
            return RedFlagSignResponse.builder().type(1).message("RED FLAG SIGN").build();
        }

        // 기타 부위 질문 요청 메시지
        List painAreas = Arrays.asList(request.getPainArea());
        if (painAreas.contains("눈") || painAreas.contains("뒷목") || painAreas.contains("턱") || painAreas.contains("얼굴피부")) {
            return RedFlagSignResponse.builder().type(4).message("선택한 통증 부위 중 하나를 요청하세요").build();
        }

        List<QuestionDto> questions = getQuestionDtos(Type.PRIMARYHEADACHEC);

        //  만성 일차성 두통 공통 질문
        if (isChronicPain(request)) {
            return RedFlagSignResponse.builder().type(3).message("만성 일차성 두통 공통 질문").questions(questions).build();
        }

        // 일차성 두통 공통 질문
        return RedFlagSignResponse.builder().type(2).message("일차성 두통 공통 질문").questions(questions).build();
    }

    /**
     * List<Question> -> List<QuestionDto> 변환 로직
     * parameter: Type Enum
     */
    private List<QuestionDto> getQuestionDtos(Type type) {
        List<QuestionDto> questions = new ArrayList<>();

        for(Question question : questionRepository.findByType(type.label())) {
            questions.add(new QuestionDto(question));
        }
        return questions;
    }

    /**
     * 만성 두통 감별 로직
     */
    private boolean isChronicPain(RedFlagSignRequest request) {
        List<RedFlagSignRequest.QnA> chronicQuestions = request.getQuestions().stream().filter(qnA -> qnA.getQuestionId() == 100 || qnA.getQuestionId() == 101).collect(Collectors.toList());
        boolean isChronic = false;

        for (RedFlagSignRequest.QnA q : chronicQuestions) {
            if (q.getAnswerId() == 0) {
                isChronic = true;
            }
        }
        return isChronic;
    }

    /**
     * Red Flag Sign 감별 로직
     */
    private boolean isRedFlagSign(RedFlagSignRequest request) {
        List<RedFlagSignRequest.QnA> redFlagQuestions = request.getQuestions().stream().filter(qnA -> qnA.getQuestionId() / 100 == 2).collect(Collectors.toList());

        boolean isRedFlag = false;
        List<RedFlagSignRequest.QnA> redFlagResult = new ArrayList<>(); // 진단 결과

        for (RedFlagSignRequest.QnA q : redFlagQuestions) {
            switch (q.getQuestionId()) {
                case 200:
                    if (Arrays.asList(0, 1).contains(q.getAnswerId())) {
                        isRedFlag = true;
                        redFlagResult.add(q);
                    }
                    break;
                case 201:
                    if (Arrays.asList(0, 1, 2, 3, 4, 5).contains(q.getAnswerId())) {
                        isRedFlag = true;
                        redFlagResult.add(q);
                    }
                    break;
                case 202:
                    if (Arrays.asList(0, 1, 2, 3).contains(q.getAnswerId())) {
                        isRedFlag = true;
                        redFlagResult.add(q);
                    }
                    break;
            }
        }
        return isRedFlag;
    }

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
}
