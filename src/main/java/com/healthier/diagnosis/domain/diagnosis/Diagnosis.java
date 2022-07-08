package com.healthier.diagnosis.domain.diagnosis;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

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
    private String h2;
    private int severity;
    private List<String> explanation;
    private Cause cause;
    private List<Solution> solutions;
    private int medicine_flag;
    private List<Medicine> medicines;
    private List<Treatment> treatments;
}
