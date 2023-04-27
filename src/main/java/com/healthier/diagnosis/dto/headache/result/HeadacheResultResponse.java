package com.healthier.diagnosis.dto.headache.result;

import lombok.Data;

@Data
public class HeadacheResultResponse {
    private int type;
    private HeadacheResult results;

    protected HeadacheResultResponse() { }

    public HeadacheResultResponse(int type, HeadacheResult results) {
        this.type = type;
        this.results = results;
    }
}
