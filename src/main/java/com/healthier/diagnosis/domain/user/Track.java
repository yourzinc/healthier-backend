package com.healthier.diagnosis.domain.user;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Data
public class Track {
    private String question_id;
    private int answer_id;
}
