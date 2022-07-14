package com.healthier.diagnosis.domain.user;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

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
    private ArrayList<Integer> interests;
    private ArrayList<UserRecord> user_records;
}
