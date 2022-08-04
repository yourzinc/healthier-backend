package com.healthier.diagnosis.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.algorithms.Algorithm;
import com.healthier.diagnosis.domain.oauth.JwtProperties;
import com.healthier.diagnosis.domain.user.User;
import com.healthier.diagnosis.exception.CustomException;
import com.healthier.diagnosis.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Base64;

import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    public String createToken(User user){
        String jwtToken = JWT.create()

                .withIssuer("auth0")
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis()+ JwtProperties.EXPIRED_TIME))

                .withClaim("email", user.getEmail())
                .withClaim("nickname", user.getNickname())

                .sign(Algorithm.HMAC256(JwtProperties.SECRET));

        return jwtToken;
    }

    public String getUserEmail(String jwtToken) {
        jwtToken = jwtToken.substring(7);
        String email = "null";
        try{
            JWTVerifier verifier =  JWT.require(Algorithm.HMAC256(JwtProperties.SECRET))
                    .withIssuer("auth0")
                    .build();
            email = verifier.verify(jwtToken)
                    .getClaim("email").asString();
        } catch (JWTDecodeException ex) {
        }
        throw new CustomException(ErrorCode.UN_AUTHORIZED);
    }
}
