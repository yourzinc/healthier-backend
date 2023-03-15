package com.healthier.diagnosis.controller;

import com.healthier.diagnosis.domain.headache.Question;
import com.healthier.diagnosis.dto.headache.QuestionResponse;
import com.healthier.diagnosis.dto.headache.painArea.HeadachePainAreaFirstRequest;
import com.healthier.diagnosis.dto.headache.painArea.HeadachePainAreaNextRequest;
import com.healthier.diagnosis.dto.headache.painArea.HeadachePainAreaNextResponse;
import com.healthier.diagnosis.service.HeadacheQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value="/api/v2/diagnose/headache")
@RestController
public class HeadacheQuestionController {
    private final HeadacheQuestionService questionService;

    /**
     * 두통 기본 질문
     */
    @GetMapping("api/v2/diagnose/headache/basic")
    public QuestionResponse BasicQuestion() {
        return new QuestionResponse(questionService.findBasicQuestion());
    }

    /**
     * 특정 통증 부위 시작 질문
     */

    // TODO : Exception 처리
    // TODO : 눈, 눈 주위
    @PostMapping("/pain-area/first")
    public QuestionResponse PainAreaFirstQuestion(@RequestBody @Valid HeadachePainAreaFirstRequest request) {
        Optional<Question> question = questionService.findPainAreaFirstQuestion(request.getPainArea());
        return new QuestionResponse(question.get());
    }

    /**
     * 특정 통증 부위 다음 질문
     */
    @PostMapping("/pain-area/next")
    public HeadachePainAreaNextResponse PainAreaNextQuestion(@RequestBody @Valid HeadachePainAreaNextRequest request) {
        return questionService.findPainAreaNextQuestion(request.getQuestionId(), request.getAnswerId());
    }

    /**
     * 추가적인 악화요인 질문
     */
    @GetMapping("/additional-factor")
    public QuestionResponse AdditionalFactorQuestion() {
        return new QuestionResponse(questionService.findAdditionalFactorQuestion());
    }
}
