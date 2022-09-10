package com.healthier.diagnosis.domain.question;

import jdk.nashorn.internal.objects.annotations.Property;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Data
@Document(collection = "diagnosis_logic")
public class Question {

    @Id
    private String id;
    private String question;

    @Field(name = "is_multiple")
    private int isMultiple;
    private ArrayList<Answer> answers;
    private int siteid;

    @Field(name = "is_default")
    private int isDefault;
    private String type;
}
