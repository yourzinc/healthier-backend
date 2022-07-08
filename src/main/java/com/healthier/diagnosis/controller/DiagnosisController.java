package com.healthier.diagnosis.controller;

import com.healthier.diagnosis.domain.diagnosis.Diagnosis;
import com.healthier.diagnosis.repository.DiagnosisRepository;
import com.healthier.diagnosis.service.DiagnosisService;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@CrossOrigin
@RequiredArgsConstructor
@RestController
public class DiagnosisController {
    private final DiagnosisService diagnosisService;
    private final DiagnosisRepository diagnosisRepository;

    @GetMapping(value = "/api/diagnosis/sleepdisorder/results/{id}")
    public ResponseEntity<?> getDiagnosis(@PathVariable String id) {
        return ResponseEntity.ok(diagnosisService.findDiagnosis(id));
    }
}
