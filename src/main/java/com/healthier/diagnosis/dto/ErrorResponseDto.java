package com.healthier.diagnosis.dto;

import com.healthier.diagnosis.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponseDto {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final String error;
    private final String message;

    public static ResponseEntity<ErrorResponseDto> toResponseEntity(ErrorCode errorCode){
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResponseDto.builder()
                        .error(errorCode.getHttpStatus().name())
                        .message(errorCode.getMessage())
                        .build()
                );
    }
}