package com.healthier.diagnosis.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /**
     *  400 BAD_REQUEST
     */
    DIAGNOSIS_NOT_FOUND(BAD_REQUEST, "해당 진단 정보를 찾을 수 없습니다."),
    QUESTION_NOT_FOUND(BAD_REQUEST, "해당 질문 정보를 찾을 수 없습니다."),
    ANSWER_NOT_FOUND(BAD_REQUEST, "해당 응답 정보를 찾을 수 없습니다."),
    RANGE_NOT_SATISFIABLE(BAD_REQUEST, "해당 요청 정보는 범위를 벗어났습니다."),
    USER_NOT_FOUND(BAD_REQUEST, "해당 유저 정보를 찾을 수 없습니다."),
    RECORD_NOT_FOUND(NO_CONTENT,"해당 유저의 진단 기록 정보를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
