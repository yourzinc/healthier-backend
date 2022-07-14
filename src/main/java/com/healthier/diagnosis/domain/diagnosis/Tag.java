package com.healthier.diagnosis.domain.diagnosis;

import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;

@Getter
@Data
public class Tag {
    private String cause;
    private ArrayList<String> details;
}
