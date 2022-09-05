package com.healthier.diagnosis.domain.user;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Data
public class Track {
    private String question_id;
    private List<Integer> answer_id;
}
