package com.garden.back.auth.jwt;

import com.garden.back.auth.jwt.response.TokenResponse;
import com.garden.back.member.Member;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class TokenProvider {

    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;
    private final Key key;

    public TokenProvider(
        JwtProperties jwtProperties,
        CollectionRefreshTokenRepository refreshTokenRepository
    ) {
        this.jwtProperties = jwtProperties;
        this.refreshTokenRepository = refreshTokenRepository;
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getTokenSecret());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenResponse generateTokenDto(Member member) {
        long now = (new Date().getTime());

        Date accessTokenExpiredDate = new Date(now + jwtProperties.getAccessTokenExpireTime());
        String accessToken = Jwts.builder()
            .setSubject(member.getEmail())
            .claim(jwtProperties.getAuthorityKey(), member.getRole().toString())
            .setExpiration(accessTokenExpiredDate)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

        Date refreshTokenExpiredDate = new Date(now + jwtProperties.getRefreshTokenExpireTime());
        String refreshToken = Jwts.builder()
            .setExpiration(refreshTokenExpiredDate)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

        RefreshToken refreshTokenObject = new RefreshToken(refreshToken, member, refreshTokenExpiredDate);
        refreshTokenRepository.save(refreshTokenObject);

        return new TokenResponse(
            jwtProperties.getBearerPrefix(),
            accessToken,
            refreshToken,
            accessTokenExpiredDate.getTime(),
            refreshTokenExpiredDate.getTime());
    }
}
