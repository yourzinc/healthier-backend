package com.healthier.diagnosis.domain.diagnosis;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Data
@Document(collection = "diagnosis")
public class Diagnosis {
    @Id
    @Field(name = "_id")
    private String id;
    @Field(name = "id")
    private int newId;
    private String illustration;
    private String h1;
    private String title;
    private ArrayList<String> h2;
    private int severity;
    private Explanation explanation;
    private Cause cause;
    private ArrayList<Solution> solutions;
    @Field(name = "medicine_flag")
    private int medicineFlag;
    private ArrayList<Medicine> medicines;
    private ArrayList<Treatment> treatments;
    @Field(name = "banner_illustration")
    private String bannerIllustration;

    /**
     * category : 수면장애
     */
    @Field(name = "not_sleepdisorder")
    private String notSleepdisorder; // DiagnosisId (수면장애 아님)
    @Field(name = "sleep_warning")
    private String sleepWarning; // DiagnosisId (수면습관 경고)
    @Field(name = "sleep_caution")
    private String sleepCaution; // DiagnosisId (수면습관 주의)
    @Field(name = "temporary_diagnosis")
    private String temporaryDiagnosis; // DiagnosisId (일시적 불면증)
    @Field(name = "short_diagnosis")
    private String shortDiagnosis; // DiagnosisId (단기 불면증)
    @Field(name = "long_diagnosis")
    private String longDiagnosis; // DiagnosisId (만성 불면증)

    /**
     * category : 두통
     */
    private String MOH; // DiagnosisId (약물과용 두통)
    @Field(name = "mild_headache")
    private String mildHeadache; // DiagnosisId (경미한 두통)
    @Field(name = "warning_headache")
    private String warningHeadache; // DiagnosisId (중증 두통)
    @Field(name = "severe_headache")
    private String severeHeadache; // DiagnosisId (만성 두통)
}
