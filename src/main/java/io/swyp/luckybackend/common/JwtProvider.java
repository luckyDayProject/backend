package io.swyp.luckybackend.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtProvider {
    @Value("${secret-key}")
    private String secretKey;

    public String create(String userNo) {
        Date expiredDate = Date.from(Instant.now().plus(1, ChronoUnit.HOURS));
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setSubject(userNo).setIssuedAt(new Date()).setExpiration(expiredDate)
                .compact();
    }

    public long validate(String jwt) {
        long subject = 0L;
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        try{
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();

            subject = Long.parseLong(claims.getSubject());
        } catch (Exception exception){
            exception.printStackTrace();
            return 0L;
        }
        return subject;
    }
}
