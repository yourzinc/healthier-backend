package com.healthier.diagnosis.domain.diagnosis;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
@Data
public class Tag {
    private String cause;
    private List<String> details;
}
