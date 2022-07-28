package com.healthier.diagnosis.domain.diagnosis;

import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;

@Getter
@Data
public class Medicine {
    private String image;
    private String name;
    private String efficacy;
    private ArrayList<DosageAndUse> dosage_and_uses;
    private Caution caution;
    private ArrayList<Sideeffect> sideeffects;

}
