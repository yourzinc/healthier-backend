package com.healthier.diagnosis.domain.diagnosis;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Data
@Document(collection = "diagnosis")
public class Diagnosis {
    @Id
    private String id;
    private String illustration;
    private String h1;
    private String title;
    private ArrayList<String> h2;
    private int severity;
    private Explanation explanation;
    private Cause cause;
    private ArrayList<Solution> solutions;
    private int medicine_flag;
    private ArrayList<Medicine> medicines;
    private ArrayList<Treatment> treatments;
    private String not_sleepdisorder; // DiagnosisId (수면장애 아님)
    private String sleep_warning; // DiagnosisId (수면습관 경고)
    private String sleep_caution; // DiagnosisId (수면습관 주의)
    private String temporary_diagnosis; // DiagnosisId (일시적 불면증)
    private String short_diagnosis; // DiagnosisId (단기 불면증)
    private String long_diagnosis; // DiagnosisId (만성 불면증)
    private String MOH; // DiagnosisId (약물과용 두통)
    private String mild_headache; // DiagnosisId (경미한 두통)
    private String warning_headache; // DiagnosisId (중증 두통)
    private String severe_headache; // DiagnosisId (만성 두통)
    private String banner_illustration;
}
