package com.healthier.diagnosis.domain.headache;

import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
public class Answer {
    @Field(name = "answer_id")
    private int answerId;
    private String answer;
    private String tag;
    @Field(name = "is_decisive")
    private boolean isDecisive;
    @Field(name = "next_question_id")
    private int nextQuestionId;
    private String question;
    @Field(name = "result_id")
    private int resultId;
    private String result;
}

