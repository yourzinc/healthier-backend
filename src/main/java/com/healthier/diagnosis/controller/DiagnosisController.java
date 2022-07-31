package com.healthier.diagnosis.controller;

import com.healthier.diagnosis.domain.diagnosis.Diagnosis;
import com.healthier.diagnosis.repository.DiagnosisRepository;
import com.healthier.diagnosis.service.DiagnosisService;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value="/api/diagnosis")
@RestController
public class DiagnosisController {
    private final DiagnosisService diagnosisService;

    @GetMapping(value = "/results/{id}")
    public ResponseEntity<?> getDiagnosis(@PathVariable String id) {
        return ResponseEntity.ok(diagnosisService.findDiagnosis(id));
    }

}
