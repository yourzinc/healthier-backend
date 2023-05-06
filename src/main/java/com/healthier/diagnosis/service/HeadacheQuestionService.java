package com.healthier.diagnosis.service;

import com.healthier.diagnosis.domain.headache.Answer;
import com.healthier.diagnosis.domain.headache.Question;
import com.healthier.diagnosis.domain.headache.PainArea;
import com.healthier.diagnosis.dto.headache.HeadacheResponse;
import com.healthier.diagnosis.dto.headache.ResultDto;
import com.healthier.diagnosis.domain.headache.Type;
import com.healthier.diagnosis.dto.headache.QuestionDto;
import com.healthier.diagnosis.dto.headache.primaryHeadache.*;
import com.healthier.diagnosis.dto.headache.primaryHeadacheNext.PrimaryHeadacheNextRequest;
import com.healthier.diagnosis.dto.headache.primaryHeadacheNext.PrimaryHeadacheNextResponse;
import com.healthier.diagnosis.dto.headache.redFlagSign.RedFlagSignRequest;
import com.healthier.diagnosis.dto.headache.redFlagSign.RedFlagSignResponse;
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
    private static final int [] PAIN_LEVEL_CHECK_QUESTION = { 404, 405, 406 };
    private static final int RED_FLAG_SIGN_RESULT_ID = 1031;
    private static final int UNKNOWN_EYE_DISEASE_RESULT_ID = 1033;

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
     * type 1 : Red Flag Sign 진단
     * type 2 : 두통 공통 질문 (일차성 / 만성 일차성)
     * type 3 : 기타 부위 질문 요청 메시지
     */
    public RedFlagSignResponse findRedFlagSignResult(RedFlagSignRequest request) {
        // type 1: Red Flag Sign 진단
        if (isRedFlagSign(request)) {
            return RedFlagSignResponse.builder().type(1).message("RED FLAG SIGN").result(new ResultDto(RED_FLAG_SIGN_RESULT_ID, "두통의 위험 신호 (RED FLAG SIGN)")).build();
        }

        boolean isChronic = isChronicPain(request);

        // type 3: 기타 부위 질문 요청 메시지
        List painAreas = request.getPainArea();
        if (painAreas.contains(PainArea.EYES.label()) || painAreas.contains(PainArea.BACKOFNECK.label()) || painAreas.contains(PainArea.CHIN) || painAreas.contains(PainArea.FACIALSKIN.label())) {
            return RedFlagSignResponse.builder().type(3).isChronic(isChronic ? 1 : 0).message("선택한 통증 부위 중 하나를 요청하세요").build();
        }

        List<Question> questions = questionRepository.findByType(Type.PRIMARYHEADACHEC.label());
        List<QuestionDto> questionDtos = getQuestionDtos(questions);

        //  type 2: 두통 공통 질문
        return RedFlagSignResponse.builder().type(2).isChronic(isChronic ? 1 : 0).message(isChronic ? "만성 일차성 두통 공통 질문" : "일차성 두통 공통 질문").questions(questionDtos).build();
    }

    /**
     * 일차성 두통 감별로직 공통질문
     */
    public List<Question> getPrimaryHeadacheQuestion() {
        return questionRepository.findByType(Type.PRIMARYHEADACHEC.label());
    }

    /**
     * 일차성 두통 공통 질문 결과
     * type 1 : 편두통 질문
     * type 2 : 긴장성/군발성 두통 질문
     */
    public HeadacheResponse findPrimaryHeadacheQuestion(PrimaryHeadacheRequest request) {
        // 공통 질문 301,302,304번 포인트 계산
        List<PrimaryHeadacheRequest.QnARequest> pointQuestions = request.getQuestions().stream().filter(qnA -> qnA.getQuestionId() != 303).collect(Collectors.toList());
        int point = 0;

        for (PrimaryHeadacheRequest.QnARequest q : pointQuestions) {
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
        PrimaryHeadacheRequest.QnARequest question303 = request.getQuestions().stream().filter(qnA -> qnA.getQuestionId() == 303).findAny().get();
        if (question303.getAnswerId() == 1) { // 군발 ++
            point ++;
        }
        else if (request.getIsChronic() == 0) { // 일차성 두통 - 편두통 ++
            point --;
        }
        // 만성 일차성 두통 - 편두통/긴장 ++ -> point 처리 X

        // 편두통 vs 긴장 판별
        if (point > 0) { // type 2: 긴장성/군발성 두통 질문
            return getHeadacheResponse(320, 2, "긴장성/군발성 두통 질문");
        }
        else { // type 1: 편두통 질문
            return getHeadacheResponse(310, 1, "편두통 질문");

        }

    }

    /**
     * 일차성 두통 질문 응답
     * type 1 : 다음 질문
     * type 2 : 진단 결과 안내
     * type 2 - 예외: 원인 불명의 안과 질환
     */
    public PrimaryHeadacheNextResponse findPrimaryHeadacheNextQuestion(PrimaryHeadacheNextRequest request) {
        Question question = questionRepository.findById(request.getQuestionId()).get();
        Answer answer = question.getAnswers().get(request.getAnswerId());

        if (answer.isDecisive()) { // 진단 결과 안내
            if (question.getId() == 332 & answer.getAnswerId() == 1 & request.getUnknownEmergency() == 1) { // type 2 - 예외: 원인 불명의 안과 질환
                return PrimaryHeadacheNextResponse.builder().type(2).result(new PrimaryHeadacheNextResponse.Result(UNKNOWN_EYE_DISEASE_RESULT_ID, "원인 불명의 안과질환")).build();
            }
            // type 2: 진단 결과 안내
            return PrimaryHeadacheNextResponse.builder().type(2).result(new PrimaryHeadacheNextResponse.Result(answer.getResultId(), answer.getResult())).build();
        }
        else { // type 1: 다음 질문
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
        List<RedFlagSignRequest.QnA> chronicQuestions = request.getQuestions().stream().filter(qnA -> qnA.getQuestionId() == 100 || qnA.getQuestionId() == 101).collect(Collectors.toList());
        boolean isChronic = false;

        for (RedFlagSignRequest.QnA q : chronicQuestions) {
            if (q.getAnswerId().contains(0)) {
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
                    if (q.getAnswerId().stream().anyMatch(
                            a -> a == 0 || a == 1
                    )) {
                        isRedFlag = true;
                        redFlagResult.add(q);
                    }
                    break;
                case 201:
                    if (q.getAnswerId().stream().anyMatch(
                            a -> a == 0 || a == 1 || a == 2 || a == 3 || a == 4 || a == 5
                    )) {
                        isRedFlag = true;
                        redFlagResult.add(q);
                    }
                    break;
                case 202:
                    if (q.getAnswerId().stream().anyMatch(
                            a -> a == 0 || a == 1  || a == 2 || a == 3
                    )) {
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
     * type 3 : ID - 404, 405, 406 (통증 수치 질문) 반환
     * type 4 : 일차성 두통 감별로직 공통질문 요청
     */
    public HeadachePainAreaNextResponse findPainAreaNextQuestion(int questionId, int answerId) {
        Optional<Question> question = questionRepository.findById(questionId);
        Answer answer = question.get().getAnswers().get(answerId); //답변 정보

        //다음 질문이 존재할 때
        if (!answer.isDecisive()) {
            int nextQuestionId = answer.getNextQuestionId(); //다음 질문 id

            // type 4: 일차성 두통 감별로직 공통질문 요청
            if (nextQuestionId == 0) {
                int unknownEmergency = 0;

                if (questionId == 406 & answerId == 0) { // 원인 불명의 안과질환 가능성 판별
                    unknownEmergency = 1;
                }
                return new HeadachePainAreaNextResponse(4, "일차성 두통 감별로직 공통질문을 요청하세요", unknownEmergency);
            }

            // type 3: 통증 수치 질문
            if (Arrays.stream(PAIN_LEVEL_CHECK_QUESTION).anyMatch(i -> i == nextQuestionId)) {
                return new HeadachePainAreaNextResponse(3, questionRepository.findById(nextQuestionId).get());
            }
            // type 1: 일반 질문
            else {
                return new HeadachePainAreaNextResponse(1, questionRepository.findById(nextQuestionId).get());
            }
        }

        // type 2: 진단 결과 안내
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
