package com.healthier.diagnosis.domain.user;

import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

public class Diagnosis {
    private String diagnosis_id;

    @CreatedDate
    private LocalDateTime is_created;
    private int severity;
}