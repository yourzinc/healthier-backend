package com.healthier.diagnosis.domain.log;

import com.healthier.diagnosis.domain.user.Track;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Document(collection = "diagnosis_log")
public class Log {
    private String diagnosis_id;
    private int birthyear;
    private String gender;

    @CreatedDate
    private LocalDateTime is_created;
    private List<Integer> interests;
    private List<Track> tracks;   // 질문 - 답변 추적
}
