package com.healthier.diagnosis.dto.headache.result;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HeadacheResultDto {
    private int id;
    private String content;
    private String bannerImg;

    protected HeadacheResultDto() {}
    public HeadacheResultDto(int id, String content, String bannerImg) {
        this.id = id;
        this.content = content;
        this.bannerImg = bannerImg;
    }

}
