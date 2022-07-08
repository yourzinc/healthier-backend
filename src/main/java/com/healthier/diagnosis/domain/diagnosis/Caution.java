package com.healthier.diagnosis.domain.diagnosis;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
@Data
public class Caution {
    private String h1;
    private String h2;
    private List<String> is_colored;
}
