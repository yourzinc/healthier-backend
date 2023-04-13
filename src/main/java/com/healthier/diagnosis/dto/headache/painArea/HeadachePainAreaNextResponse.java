package com.healthier.diagnosis.dto.headache.painArea;

import com.healthier.diagnosis.domain.headache.Question;
import com.healthier.diagnosis.dto.headache.QuestionDto;
import com.healthier.diagnosis.dto.headache.ResultDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HeadachePainAreaNextResponse {
    private int type;
    private List<QuestionDto> questions = new ArrayList<>();
    ResultDto result;
    private String message;
    private int unknownEmergency;

    //생성자
    protected HeadachePainAreaNextResponse() {}

    //1) 생성자 : 다음 질문 반환
    public HeadachePainAreaNextResponse(int type, Question question) {
        this.type = type;
        questions.add(new QuestionDto(question));
    }

    //2) 생성자 : 진단 결과 안내
    public HeadachePainAreaNextResponse(int resultId, String result) {
        type = 2;
        this.result = new ResultDto(resultId, result);
    }

    //3) 생성자 : 일차성 두통 감별로직 공통질문 요청 + 예외 가능성 변수
    public HeadachePainAreaNextResponse(int type, String message, int unknownEmergency) {
        this.type = type;
        this.message = message;
        this.unknownEmergency = unknownEmergency;
    }
}
