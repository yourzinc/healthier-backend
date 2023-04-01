package com.healthier.diagnosis.dto.headache.result;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class HeadacheResultDto {
    private int id;
    private String content;
    private String bannerIllustration;

    protected HeadacheResultDto() {}
    public HeadacheResultDto(int id, String content, String bannerImg) {
        this.id = id;
        this.content = content;
        this.bannerIllustration = bannerImg;
    }

}
