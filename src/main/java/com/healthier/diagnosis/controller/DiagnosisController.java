package com.healthier.diagnosis.controller;

import com.healthier.diagnosis.service.DiagnosisService;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value="/api")
@RestController
public class DiagnosisController {
    private final DiagnosisService diagnosisService;

    @GetMapping(value = "/diagnosis/results/{id}")
    public ResponseEntity<?> getDiagnosis(@PathVariable String id) {
        return ResponseEntity.ok(diagnosisService.findDiagnosis(id));
    }

    @GetMapping(value = "/v2/diagnose/headache/results/{id}")
    public ResponseEntity<?> getHeadacheDiagnosisResultDetail(@PathVariable int id) {
        return ResponseEntity.ok(diagnosisService.getHeadacheResultDetail(id));
    }
}
