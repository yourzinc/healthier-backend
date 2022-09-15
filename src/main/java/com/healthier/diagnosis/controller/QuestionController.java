package com.healthier.diagnosis.controller;

import com.healthier.diagnosis.dto.*;
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

    @PostMapping
    public ResponseEntity<?> getNextQuestion(@RequestBody @Valid QuestionRequestDto dto) {
        return ResponseEntity.ok(questionService.findNextQuestion(dto));
    }

    // 수면장애 초기 질문
    @GetMapping (value = "/sleepdisorder/first")
    public ResponseEntity<?> getSleepdisorderFirstQuestion() {
        return ResponseEntity.ok(questionService.findFirstQuestion("sleepdisorder"));
    }

    @PostMapping (value = "/sleepdisorder/decisive")
    public ResponseEntity<?> getDecisiveQuestion(@RequestBody @Valid DecisiveQuestionRequestDto dto) {
        return ResponseEntity.ok(questionService.findDecisiveQuestion(dto));
    }

    // 두통 초기 질문
    @GetMapping (value =  "/headache/first")
    public ResponseEntity<?> getHeadacheFirstQuestion(){
        return ResponseEntity.ok(questionService.findFirstQuestion("headache"));
    }

    @PostMapping (value = "/headache/decisive")
    public ResponseEntity<?> getHeadacheDecisiveQuestion(@RequestBody @Valid HeadacheDecisiveQuestionRequestDto dto) {
        return ResponseEntity.ok(questionService.findHeadacheDecisiveQuestion(dto));
    }
}
