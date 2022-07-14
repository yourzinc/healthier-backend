package com.healthier.diagnosis.domain.diagnosis;

import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;

@Getter
@Data
public class Caution {
    private String h1;
    private String h2;
    private ArrayList<String> is_colored;
}
