package com.healthier.diagnosis.domain.user;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Data
@Document(collection = "user")
public class User {
    @Id
    private String id;
    private int birth_year;
    private String gender;
    private List<Integer> interests;
    private List<UserRecord> user_records;
}
