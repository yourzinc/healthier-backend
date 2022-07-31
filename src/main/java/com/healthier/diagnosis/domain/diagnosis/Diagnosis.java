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
    private String temporary_diagnosis;
    private String short_diagnosis;
    private String long_diagnosis;
    private String MOH;
    private String mild_headache;
    private String warning_headache;
    private String severe_headache;
    private String banner_illustration;
}
