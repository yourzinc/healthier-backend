package com.healthier.diagnosis.domain.user;

import com.healthier.diagnosis.domain.diagnosis.Diagnosis;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Data
@Document(collection = "user")
public class User {

    @Id
    private String id;
    private String nickname;
    private String email;
    private List<Record> records;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Record {
        private String diagnosis_id;
        @CreatedDate
        private LocalDateTime is_created;
        private int severity;
        private String title;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseRecord {
        private Record Record;
        private String banner_illustration;
    }
}
