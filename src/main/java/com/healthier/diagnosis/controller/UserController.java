package com.healthier.diagnosis.controller;

import com.healthier.diagnosis.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping(value="/api")
@RestController
public class UserController {
    private final UserService userService;

    @GetMapping("oauth/kakao")
    public ResponseEntity<?> login(@RequestParam("access_token") String accessToken) {

        // access token으로 회원가입 후 jwt 생성
        String jwtToken = userService.getToken(accessToken);

        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtProperties.HEADER.STRING, JwtProperties.TOKEN_PREFIX + jwtToken);

        return ResponseEntity.ok().header(headers).body("success");
    }
}
