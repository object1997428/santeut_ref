package com.santeut.hiking.common;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class JwtUtilTest {
    @Autowired
    JwtUtil jwtUtil;

    private final SecretKey secretKey;

    JwtUtilTest(@Value("${hiking.token.secretkey}") String secretString) {
        secretKey= Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretString));
    }

    @Test
    void 토큰_생성하기() {
        //given
        var claims = new HashMap<String, Object>();
        claims.put("user_id", 1);
        claims.put("user_nickname","object1997");
        claims.put("party_id",3);
        claims.put("user_party_id",5);
        claims.put("user_profile","abc");

        //when
        String jwtTokenString = jwtUtil.createToken(60000, claims);

        //then
        assertThat(jwtUtil.getUserId(jwtTokenString)).isEqualTo(1);
        assertThat(jwtUtil.getPartyId(jwtTokenString)).isEqualTo(3);
        assertThat(jwtUtil.getPartyUserId(jwtTokenString)).isEqualTo(5);
        assertThat(jwtUtil.getUserNickname(jwtTokenString)).isEqualTo("object1997");
        assertThat(jwtUtil.getUserProfile(jwtTokenString)).isEqualTo("abc");
        assertThat(jwtUtil.getExpireDate(jwtTokenString).after(new Date()));
    }

    @Test
    void 커뮤니티서버에서_생성된_jwt_검증하기(){
        String jwtToken="eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxLCJwYXJ0eV9pZCI6MywidXNlcl9uaWNrbmFtZSI6Im9iamVjdDE5OTciLCJ1c2VyX3Byb2ZpbGUiOiJhYmMiLCJ1c2VyX3BhcnR5X2lkIjo1LCJpc3MiOiJjb21tdW5pdHlfc2VydmVyIiwiaWF0IjoxNzU1NjczMTA1LCJleHAiOjE3NTU3NTk1MDV9.C9OChmJ4Sc2i7v3o2Rw-pYkqE7mKaYPFBIhklTBynnw";

        jwtUtil.validateToken(jwtToken);
        assertThat(jwtUtil.getUserId(jwtToken)).isEqualTo(1);
        assertThat(jwtUtil.getUserNickname(jwtToken)).isEqualTo("object1997");
        assertThat(jwtUtil.getUserProfile(jwtToken)).isEqualTo("abc");
        assertThat(jwtUtil.getPartyId(jwtToken)).isEqualTo(3);
        assertThat(jwtUtil.getPartyId(jwtToken)).isEqualTo(3);
    }

    @Test
    void 잘못된_비밀키로_사인된_토큰_검증하기() {
        /**
         * 상황: 잘못된 secretKey로 서명된 토큰을 검증하려고 할때
         * 기대결과: 검증실패, RuntimeException 예외 발생
         */

        SecretKey wrongKey = Jwts.SIG.HS256.key().build();

        var claims = new HashMap<String, Object>();
        claims.put("user_id", 1);
        claims.put("user_nickname","object1997");
        claims.put("party_id",3);
        claims.put("user_party_id",5);
        claims.put("user_profile","abc");

        String jwtTokenString = Jwts.builder()
                .claims(claims)
                .issuer("hiking_server")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+60000))
                .signWith(wrongKey)
                .compact();

        RuntimeException e = Assertions.assertThrows(RuntimeException.class, () -> jwtUtil.validateToken(jwtTokenString));
        assertThat(e.getMessage()).isEqualTo("JWT Token Invalid Exception");
    }

    @Test
    void 잘못된_ISSUER로_사인된_토큰_검증하기() {
        /**
         * 상황: 잘못된 ISSUER로 서명된 토큰을 검증하려고 할때
         * 기대결과: 검증실패, RuntimeException 예외 발생
         */
        var claims = new HashMap<String, Object>();
        claims.put("user_id", 1);
        claims.put("user_nickname","object1997");
        claims.put("party_id",3);
        claims.put("user_party_id",5);
        claims.put("user_profile","abc");

        String jwtTokenString = Jwts.builder()
                .claims(claims)
                .issuer("hiking_server")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+60000))
                .signWith(secretKey)
                .compact();

        RuntimeException e = Assertions.assertThrows(RuntimeException.class, () -> jwtUtil.validateToken(jwtTokenString));
        assertThat(e.getMessage()).isEqualTo("JWT Token Incorrect ISSUER Exception");
    }

    @Test
    void 만료된_토큰_검증하기() {
        /**
         * 상황: 만료된 토큰을 검증할때
         * 기대결과: 검증실패, RuntimeException 예외 발생
         */

        var claims = new HashMap<String, Object>();
        claims.put("user_id", 1);
        claims.put("user_nickname","object1997");
        claims.put("party_id",3);
        claims.put("user_party_id",5);
        claims.put("user_profile","abc");

        String jwtTokenString = jwtUtil.createToken(0, claims);


        RuntimeException e = Assertions.assertThrows(RuntimeException.class, () -> jwtUtil.validateToken(jwtTokenString));
        assertThat(e.getMessage()).isEqualTo("JWT Token Expired Exception");
    }


    @Test
    void 유저_아이디_얻기() {
        var claims = new HashMap<String, Object>();
        claims.put("user_id", 1);
        String jwtTokenString = jwtUtil.createToken(60000, claims);

        assertThat(jwtUtil.getUserId(jwtTokenString)).isEqualTo(1);
    }

    @Test
    void 소모임_아이디_얻기() {
        var claims = new HashMap<String, Object>();
        claims.put("party_id",3);

        String jwtTokenString = jwtUtil.createToken(60000, claims);

        assertThat(jwtUtil.getPartyId(jwtTokenString)).isEqualTo(3);
    }

    @Test
    void 소모임_유저_아이디_얻기() {
        var claims = new HashMap<String, Object>();
        claims.put("user_party_id",5);

        String jwtTokenString = jwtUtil.createToken(60000, claims);

        assertThat(jwtUtil.getPartyUserId(jwtTokenString)).isEqualTo(5);
    }

    @Test
    void 유저_닉네임_얻기() {
        var claims = new HashMap<String, Object>();
        claims.put("user_nickname","object1997");

        String jwtTokenString = jwtUtil.createToken(60000, claims);

        assertThat(jwtUtil.getUserNickname(jwtTokenString)).isEqualTo("object1997");
    }

    @Test
    void 유저_프로필_얻기() {
        var claims = new HashMap<String, Object>();
        claims.put("user_profile","abc");

        String jwtTokenString = jwtUtil.createToken(60000, claims);

        assertThat(jwtUtil.getUserProfile(jwtTokenString)).isEqualTo("abc");
    }
}