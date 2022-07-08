package com.healthier.diagnosis.domain.diagnosis;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
@Data
public class Cause {
    private int tag_flag;
    private List<Tag> tags;
    private List<String> detail;
}
