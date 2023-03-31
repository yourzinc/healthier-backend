package com.healthier.diagnosis.service;

import com.healthier.diagnosis.domain.headache.Answer;
import com.healthier.diagnosis.domain.headache.Question;
import com.healthier.diagnosis.dto.headache.ResultDto;
import com.healthier.diagnosis.domain.question.Type;
import com.healthier.diagnosis.dto.headache.QuestionDto;
import com.healthier.diagnosis.dto.headache.commonQuestion.*;
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
    public HeadacheResponse findRedFlagSignResult(RedFlagSignRequest request) {
        // Red Flag Sign 진단
        if (isRedFlagSign(request)) {
            return HeadacheResponse.builder().type(1).message("RED FLAG SIGN").result(new ResultDto(1031, "두통의 위험 신호 (RED FLAG SIGN)")).build();
        }

        // 기타 부위 질문 요청 메시지
        List painAreas = request.getPainArea();
        if (painAreas.contains("눈") || painAreas.contains("뒷목") || painAreas.contains("턱") || painAreas.contains("얼굴피부")) {
            return HeadacheResponse.builder().type(4).message("선택한 통증 부위 중 하나를 요청하세요").build();
        }

        List<Question> questions = questionRepository.findByType(Type.PRIMARYHEADACHEC.label());
        List<QuestionDto> questionDtos = getQuestionDtos(questions);

        //  만성 일차성 두통 공통 질문
        if (isChronicPain(request)) {
            return HeadacheResponse.builder().type(3).message("만성 일차성 두통 공통 질문").questions(questionDtos).build();
        }

        // 일차성 두통 공통 질문
        return HeadacheResponse.builder().type(2).message("일차성 두통 공통 질문").questions(questionDtos).build();
    }

    /**
     * 일차성 두통 공통 질문 결과
     *
     * [일차성 두통 공통 질문 점수 계산 로직]
     *
     * <선택 가능 범위>
     * 예 -> (DB) 0 (연산) -1 // 한 질문에서 편두통과 긴장이 동일한 득점을 얻는 경우 정확한 계산을 하기 위함
     * 아니오 -> (DB & 연산) +1
     *
     * <득점 케이스>
     * 주로 편두통(-1) vs 군발 / 긴장(+1)
     * 특히 편두통(-1) / 긴장(+1) vs 군발(+1) 의 경우
     *
     * <판단 기준>
     * -> 값이 양수냐~ 음수냐~
     * if (point > 0) 긴장성 질문
     * if (point <= 0) 편두통 질문 // 동점이면 편두통 질문
     */
    public HeadacheResponse findPrimaryHeadacheQuestion(PrimaryHeadacheRequest request) {
        // 공통 질문 301,302,304번 포인트 계산
        List<QnARequest> pointQuestions = request.getQuestions().stream().filter(qnA -> qnA.getQuestionId() != 303).collect(Collectors.toList());
        int point = 0;

        for (QnARequest q : pointQuestions) {
            if (q.getAnswerId() == 1) { // 군발/긴장 ++
                point ++;
            }
            else if (q.getQuestionId() == 304) { // 편두통/긴장 ++
                continue;
            }
            else { // 편두통 ++
                point --;
            }
        }

        // 공통 질문 303번 포인트 계산
        QnARequest question303 = request.getQuestions().stream().filter(qnA -> qnA.getQuestionId() == 303).findAny().get();
        if (question303.getAnswerId() == 1) { // 군발 ++
            point ++;
        }
        else if (request.getType() == 2) { // 일차성 두통 - 편두통 ++
            point --;
        }
        // 만성 일차성 두통 - 편두통/긴장 ++ -> point 처리 X

        // 편두통 vs 긴장 판별
        if (point > 0) { // 긴장성 두통 질문
            return getHeadacheResponse(320, 2, "긴장성/군발성 두통 질문");
        }
        else { // 편두통 질문
            return getHeadacheResponse(310, 1, "편두통 질문");

        }

    }

    /**
     * 일차성 두통 질문 응답
     */
    public PrimaryHeadacheNextResponse findPrimaryHeadacheNextQuestion(QnARequest request) {
        Question question = questionRepository.findById(request.getQuestionId()).get();
        Answer answer = question.getAnswers().get(request.getAnswerId());

        if (answer.isDecisive()) { // 진단 결과 안내
            return PrimaryHeadacheNextResponse.builder().type(2).result(new PrimaryHeadacheNextResponse.Result(answer.getResultId(), answer.getResult())).build();
        }
        else { // 다음 질문
            List<Question> questions = new ArrayList<>();
            questions.add(questionRepository.findById(answer.getNextQuestionId()).get());

            List<QuestionDto> questionDtos = getQuestionDtos(questions);

            return PrimaryHeadacheNextResponse.builder().type(1).questions(questionDtos).build();
        }
    }

    private HeadacheResponse getHeadacheResponse(int id, int type, String message) {
        List<Question> questions = new ArrayList<>();
        questions.add(questionRepository.findById(id).get());

        List<QuestionDto> questionDtos = getQuestionDtos(questions);

        return HeadacheResponse.builder().type(type).message(message).questions(questionDtos).build();
    }

    /**
     * List<Question> -> List<QuestionDto> 변환 로직
     * parameter: List<Question>
     * Repository에서 조회한 데이터를 dto로 가공한다.
     */
    private List<QuestionDto> getQuestionDtos(List<Question> questions) {
        List<QuestionDto> questionDtos = new ArrayList<>();

        for (Question question : questions) {
            questionDtos.add(new QuestionDto(question));
        }
        return questionDtos;
    }

    /**
     * 만성 두통 감별 로직
     */
    private boolean isChronicPain(RedFlagSignRequest request) {
        List<QnARequest> chronicQuestions = request.getQuestions().stream().filter(qnA -> qnA.getQuestionId() == 100 || qnA.getQuestionId() == 101).collect(Collectors.toList());
        boolean isChronic = false;

        for (QnARequest q : chronicQuestions) {
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
        List<QnARequest> redFlagQuestions = request.getQuestions().stream().filter(qnA -> qnA.getQuestionId() / 100 == 2).collect(Collectors.toList());

        boolean isRedFlag = false;
        List<QnARequest> redFlagResult = new ArrayList<>(); // 진단 결과

        for (QnARequest q : redFlagQuestions) {
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
