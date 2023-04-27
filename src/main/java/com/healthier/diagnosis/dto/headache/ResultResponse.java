package com.healthier.diagnosis.dto.headache;

import com.healthier.diagnosis.domain.headache.Question;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResultResponse {
    private ResultDto result;

    public ResultResponse(ResultDto resultDto) {
        result = resultDto;
    }
}
