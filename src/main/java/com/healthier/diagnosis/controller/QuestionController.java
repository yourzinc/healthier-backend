package com.healthier.diagnosis.controller;

import com.healthier.diagnosis.dto.FirstQuestionRequestDto;
import com.healthier.diagnosis.dto.QuestionRequestDto;
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

    @GetMapping (value = "/sleepdisorder")
    public ResponseEntity<?> getNextQuestion(@RequestBody @Valid QuestionRequestDto dto) {
        return ResponseEntity.ok(questionService.findNextQuestion(dto));
    }

    @GetMapping (value = "/sleepdisorder/first")
    public ResponseEntity<?> getFirstQuestion(@RequestBody @Valid FirstQuestionRequestDto dto) {
        return ResponseEntity.ok(questionService.findFirstQuestion());
    }

    @PostMapping (value = "/sleepdisorder/decisive")
    public ResponseEntity<?> getDecisiveQuestion(@RequestBody @Valid DecisiveQuestionRequestDto dto) {
        return ResponseEntity.ok(questionService.findFirstQuestion());
    }
}
