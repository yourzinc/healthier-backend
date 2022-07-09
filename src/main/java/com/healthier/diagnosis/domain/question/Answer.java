package com.healthier.diagnosis.domain.question;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class Answer {
    private int answer_id;
    private String answer;
    private int is_decisive;  // 1이면 진단 결과 도출
    private String next_question_id;  // 다음 질문 Id
    private String result_id;  // 진단 title
}
