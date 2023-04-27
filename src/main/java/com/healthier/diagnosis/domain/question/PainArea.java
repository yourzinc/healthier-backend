package com.healthier.diagnosis.domain.question;

public enum PainArea {
    EYES("눈"),
    BACKOFNECK("뒷목"),
    CHIN("턱"),
    FACIALSKIN("얼굴피부")
    ;

    private final String label;

    PainArea(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
