package com.healthier.diagnosis.service;

import com.healthier.diagnosis.domain.question.Answer;
import com.healthier.diagnosis.domain.question.Question;
import com.healthier.diagnosis.domain.log.Log;
import com.healthier.diagnosis.domain.user.Track;
import com.healthier.diagnosis.dto.question.*;
import com.healthier.diagnosis.dto.question.decisiveQuestion.DecisiveQuestionRequestDto;
import com.healthier.diagnosis.dto.question.headache.HeadacheDefaultQuestionAfterRequestDto;
import com.healthier.diagnosis.exception.CustomException;
import com.healthier.diagnosis.exception.ErrorCode;
import com.healthier.diagnosis.repository.DiagnosisLogRepository;
import com.healthier.diagnosis.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
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

//    ===================== 수면장애 첫번째 진단 응답 삭제 =====================
//
//    /**
//     * 수면장애 : 수면의 문제가 일상생활에 지장을 주나요? 에 no 라고 응답한 경우, 수면위생점수로 진단
//     */
//    @Transactional(readOnly = true)
//    public Object findFirstQuestion(FirstQuestionRequestDto dto) {
//        if(dto.getAnswer().equals("n")){
//            int score = dto.getScoreB();
//            if(score < 0 || score > 17){
//                throw new CustomException(ErrorCode.RANGE_NOT_SATISFIABLE);
//            }
//            if (score <= 6){ // 수면 장애 아님
//                return diagnosisService.findDiagnosis("62ce908856e36933184b0fbd");
//            }
//            else if(score >= 11) { // 수면습관 경고
//                return diagnosisService.findDiagnosis("62ce900456e36933184b0fba");
//           }
//            else { // 수면습관 주의
//                return diagnosisService.findDiagnosis("62ce906a56e36933184b0fbc");
//            }
//        }
//
//        // 첫번째 질문 정보 조회
//        return getQuestionResponseDto(ID);
//    }
//    ===================== 수면장애 첫번째 진단 응답 삭제 =====================

    /**
     * 수면장애 : 결정적 질문 진단결과 조회 및 로그 저장
     */
    public DiagnosisResponseDto findDecisiveQuestion(DecisiveQuestionRequestDto dto) {
        Question in_question = questionRepository.findById(dto.getQuestionId())
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

        Answer in_answer = in_question.getAnswers().stream()
                .filter(i -> i.getAnswer_id() == dto.getAnswerId())
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.ANSWER_NOT_FOUND));

        String resultId = in_answer.getResult_id();

        // 수면장애가 아닌 경우 -> 수면위생점수로 결정하기
        if (resultId.equals("6322d088eaac10034c60903d")){

            // 수면위생점수 계산
            int SHI = getSHI(dto.getTracks());
            resultId = diagnosisService.findSleepdisorderWithSHI(resultId, SHI);
        }

        // 심리적 불면증 or 수면환경 불면증 -> 기간으로 결정하기
        else if (resultId.equals("62d17692f68f2b673e721211") || resultId.equals("62d176ecf68f2b673e721212")) {

            // 기간 확인
            int period = getPeriod(dto.getTracks());
            resultId = diagnosisService.findInsomniaPeriod(resultId, period);
        }

        // 진단 로그 활성화
        if (dto.getTracks() != null) {
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

        return diagnosisService.findDiagnosis(resultId);
    }

    /**
     *  수면장애 : 수면위생점수 SHI 계산
     */
    int getSHI(List<Track> tracks) {
        AtomicInteger SHI = new AtomicInteger();
        SHI.set(0);
        // tracks 에서 question - answer 확인하면서 수면 위생 점수 계산

        // 평소 알코올 섭취량? 631ad4c675ce6608eb91f75a
        // answer_id = 0 -> + 0점
        // answer_id = 1 -> + 1점
        // answer_id = 2 -> + 2점
        SHI.addAndGet(tracks.stream()
                .filter(track -> track.getQuestion_id().equals("631ad45475ce6608eb91f759"))
                .findFirst()
                .get().getAnswer_id().get(0));


        // 평소 카페인 섭취량? 631ad45475ce6608eb91f759
        // answer_id = 0 -> + 0점
        // answer_id = 1 -> + 1점
        // answer_id = 2 -> + 2점
        SHI.addAndGet(tracks.stream()
                .filter(track -> track.getQuestion_id().equals("631ad45475ce6608eb91f759"))
                .findFirst()
                .get().getAnswer_id().get(0));

        // 자신에게 해당하는 것을 고르세요. 631ad41075ce6608eb91f757
        // answer_id = 0, 1, 2, 3 -> + 1점
        // answer_id = 4, 5, 6 -> + 2점
        tracks.stream()
                .filter(track -> track.getQuestion_id().equals("631ad41075ce6608eb91f757"))
                .findFirst()
                .get()
                .getAnswer_id()
                .stream().forEach(answer -> {
                    if ( answer < 4 ) SHI.addAndGet(1); // 0, 1, 2, 3 -> 1점
                    else SHI.addAndGet(2); // 4, 5, 6 -> 2점
                });

        return SHI.get();
    }

    /**
     *  수면장애 : 기간 점수
     *
     *  ID : 631ad53675ce6608eb91f75c
     *  Q) 언제부터 잠이 안오기 시작했나요?
     *  A) 0. 일주일전 1. 2주전 2. 한달 전 3. 3개월 전
     */

    private int getPeriod(List<Track> tracks) {
        return tracks.stream()
                .filter(track -> track.getQuestion_id().equals("631ad53675ce6608eb91f75c"))
                .findFirst()
                .get().getAnswer_id().get(0);
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

    public DiagnosisResponseDto findHeadacheDecisiveQuestion(DecisiveQuestionRequestDto dto) {
        Question in_question = questionRepository.findById(dto.getQuestionId())
                .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

        Answer in_answer = in_question.getAnswers().stream()
                .filter(i -> i.getAnswer_id() == dto.getAnswerId())
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.ANSWER_NOT_FOUND));

        String resultId = in_answer.getResult_id();

        // 약물과용 두통 확인 -> is_taking_medicine 확인
        if (resultId.equals("62e11e121549f1a6fe9f58b0")){

            int is_taking_medication = get_is_taking_medication(dto.getTracks());
            int pain_level = get_pain_level(dto.getTracks());
            int period = get_period(dto.getTracks());
            int cycle = get_cycle(dto.getTracks());

            resultId = diagnosisService.checkMOH_mild_warning_severe(resultId, is_taking_medication, pain_level, period, cycle);
        }

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

        return diagnosisService.findDiagnosis(resultId);
    }

    /**
     *  두통 : 약물 복용 여부
     *
     *  ID : 631adabc75ce6608eb91f75e
     *  Q) 한 달에 15일 이상 진통제 등의 약물을 지속적으로 복용했나요?
     *  A) 0. 예  1. 아니오
     */
    private int get_is_taking_medication(List<Track> tracks) {
        return tracks.stream()
                .filter(track -> track.getQuestion_id().equals("631adabc75ce6608eb91f75e"))
                .findFirst()
                .get().getAnswer_id().get(0);
    }

    /**
     *  두통 : 통증
     *
     *  ID : 631adaf175ce6608eb91f75f
     *  Q) 통증의 정도가 어떻게 되나요?
     *  A) 1, 2, 3, 4, 5
     */
    private int get_pain_level(List<Track> tracks) {
        return tracks.stream()
                .filter(track -> track.getQuestion_id().equals("631adaf175ce6608eb91f75f"))
                .findFirst()
                .get().getAnswer_id().get(0)+1;
    }

    /**
     *  두통 : 주기
     *
     *  ID : 631adb1775ce6608eb91f760
     *  Q) 1달에 15일 이상 두통 증상이 있나요?
     *  A) 0. 예  1. 아니오
     */
    private int get_cycle(List<Track> tracks) {
        return tracks.stream()
                .filter(track -> track.getQuestion_id().equals("631adb1775ce6608eb91f760"))
                .findFirst()
                .get().getAnswer_id().get(0);
    }

    /**
     *  두통 : 시기
     *
     *  ID : 631adb3775ce6608eb91f761
     *  Q) 언제부터 통증이 시작되었나요?
     *  A) 0. 일주일 이내  1. 2주 전  2. 한달 전  3. 3개월 전
     */
    private int get_period(List<Track> tracks) {
        return tracks.stream()
                .filter(track -> track.getQuestion_id().equals("631adb3775ce6608eb91f761"))
                .findFirst()
                .get().getAnswer_id().get(0);
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
