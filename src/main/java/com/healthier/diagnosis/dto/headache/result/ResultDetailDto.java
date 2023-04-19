package com.healthier.diagnosis.dto.headache.result;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.healthier.diagnosis.domain.diagnosis.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResultDetailDto {
    private int id;
    private String illustration;
    private String h1;
    private String title;
    private ArrayList<String> h2;
    private int severity;
    private Explanation explanation;
    private Cause cause;
    private ArrayList<Solution> solutions;
    private int medicineFlag;
    private ArrayList<Medicine> medicines;
    private ArrayList<Treatment> treatments;
    private String bannerIllustration;

    protected  ResultDetailDto() { }

    public ResultDetailDto(Diagnosis diagnosis) {
        id = diagnosis.getNewId();
        illustration = diagnosis.getIllustration();
        h1 = diagnosis.getH1();
        title = diagnosis.getTitle();
        h2 = diagnosis.getH2();
        severity = diagnosis.getSeverity();
        explanation = diagnosis.getExplanation();
        cause = diagnosis.getCause();
        solutions = diagnosis.getSolutions();
        medicineFlag = diagnosis.getMedicineFlag();
        medicines = diagnosis.getMedicines();
        treatments = diagnosis.getTreatments();
        bannerIllustration = diagnosis.getBannerIllustration();
    }
}