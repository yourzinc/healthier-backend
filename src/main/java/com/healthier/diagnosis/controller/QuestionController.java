package com.healthier.diagnosis.controller;

import com.healthier.diagnosis.dto.question.decisiveQuestion.DecisiveQuestionRequestDto;
import com.healthier.diagnosis.dto.question.headache.HeadacheDefaultQuestionAfterRequestDto;
import com.healthier.diagnosis.dto.question.QuestionRequestDto;
import com.healthier.diagnosis.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value="/api/diagnose")
@RestController
public class QuestionController {

    private final QuestionService questionService;

    // 진단 질문 응답
    @PostMapping
    public ResponseEntity<?> getNextQuestion(@RequestBody @Valid QuestionRequestDto dto) {
        return ResponseEntity.ok(questionService.findNextQuestion(dto));
    }

    // 수면장애 초기 질문
    @GetMapping (value = "/sleepdisorder/first")
    public ResponseEntity<?> getSleepdisorderFirstQuestion() {
        return ResponseEntity.ok(questionService.findFirstQuestion("sleepdisorder"));
    }

    // 수면장애 결정적 진단 응답
    @PostMapping (value = "/sleepdisorder/decisive")
    public ResponseEntity<?> getDecisiveQuestion(@RequestBody @Valid DecisiveQuestionRequestDto dto) {
        return ResponseEntity.ok(questionService.findDecisiveQuestion(dto));
    }

    // 두통 초기 질문
    @GetMapping (value =  "/headache/first")
    public ResponseEntity<?> getHeadacheFirstQuestion(){
        return ResponseEntity.ok(questionService.findFirstQuestion("headache"));
    }

    // 두통 마지막 초기 질문 응답
    @PostMapping(value = "/headache/last-default")
    public ResponseEntity<?> getHeadacheDefaultQuestionAfter(@RequestBody @Valid HeadacheDefaultQuestionAfterRequestDto dto){
        return ResponseEntity.ok(questionService.findHeadacheDefaultQuestionAfter(dto));
    }

    // 두통 결정적 진단 응답
    @PostMapping (value = "/headache/decisive")
    public ResponseEntity<?> getHeadacheDecisiveQuestion(@RequestBody @Valid DecisiveQuestionRequestDto dto) {
        return ResponseEntity.ok(questionService.findHeadacheDecisiveQuestion(dto));
    }
}
