package com.healthier.diagnosis.dto.headache;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class ResultDto {
    private int result_id;
    private String result;

    protected ResultDto() {}

    public ResultDto(int result_id, String result) {
        this.result_id = result_id;
        this.result = result;
    }
}

