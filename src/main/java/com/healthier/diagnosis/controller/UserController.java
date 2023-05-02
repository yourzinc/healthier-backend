package com.healthier.diagnosis.controller;

import com.healthier.diagnosis.domain.oauth.JwtProperties;
import com.healthier.diagnosis.dto.question.saveDiagnosis.SaveDiagnosisRequestDto;
import com.healthier.diagnosis.security.jwt.JwtTokenProvider;
import com.healthier.diagnosis.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value="/api")
@RestController
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider tokenProvider;

    // 회원가입 및 로그인
    @GetMapping("/oauth/kakao")
    public ResponseEntity<?> login(@RequestParam("access_token") String accessToken) {

        // access token으로 회원가입 후 jwt 생성
        String jwtToken = userService.getToken(accessToken);

        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);

        return ResponseEntity.ok().headers(headers).body("success");
    }

    // 진단 기록장 목록
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/diagnosis/results")
    public ResponseEntity<?> getMyDiagnosis(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(userService.getDiagnosisList(token));
    }

    // 진단 결과 저장
    @PreAuthorize("isAuthenticated()")
    @PatchMapping(value = "/diagnosis/results")
    public ResponseEntity<?> saveMyDiagnosis(@RequestHeader("Authorization") String token, @RequestBody SaveDiagnosisRequestDto dto) {
        return ResponseEntity.ok(userService.saveMyDiagnosis(token, dto));
    }
}
