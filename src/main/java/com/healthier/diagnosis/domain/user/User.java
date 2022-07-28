package com.healthier.diagnosis.domain.user;

import com.healthier.diagnosis.domain.diagnosis.Diagnosis;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Data
@Document(collection = "user")
public class User {
    private String nickname;
    private String email;
    private ArrayList<Record> records;

    @Data
    @Builder
    @NoArgsConstructor
    public static class Record {
        private String diagnosis_id;
        @CreatedDate
        private LocalDateTime is_created;
        private int severity;
        private String title;
    }
}
