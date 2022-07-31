package com.healthier.diagnosis.domain.oauth;

public interface JwtProperties {
    String SECRET = "${SECRET}"; //"wearestrong";
    Long EXPIRED_TIME = 1000 * 60L * 60L * 1L;  // 2시간
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}