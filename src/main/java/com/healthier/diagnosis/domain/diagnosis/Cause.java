package com.healthier.diagnosis.domain.diagnosis;

import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;

@Getter
@Data
public class Cause {
    private int tag_flag;
    private ArrayList<Tag> tags;
    private ArrayList<String> detail;
}
