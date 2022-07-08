package com.healthier.diagnosis.domain.diagnosis;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class Solution {
    private String title;
    private String detail;
    private String emoji;
}
