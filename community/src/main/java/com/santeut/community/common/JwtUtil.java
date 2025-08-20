package com.santeut.community.common;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
public class JwtUtil {

    private final SecretKey secretKey;

    private final String issuer="community_server";


    public JwtUtil(@Value("${hiking.token.secretkey}") String secretString) {
        secretKey=Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretString));
    }


    public String createToken(long expirationTime, Map<String, ?> claims){
        return Jwts.builder()
                    .claims(claims)
                    .issuer(issuer)
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis()+expirationTime))
                    .signWith(secretKey)
                    .compact();
    }

    public Claims validateToken(String jwsString) {
        Jws<Claims> claims;
        try {
            //만료 되었는지 확인
            claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .requireIssuer(issuer)
                    .build()
                    .parseSignedClaims(jwsString);
            return claims.getPayload();
        } catch (SignatureException e) {
            throw new RuntimeException("JWT Token Invalid Exception");
        } catch (ExpiredJwtException e){
            throw new RuntimeException("JWT Token Expired Exception");
        } catch (IncorrectClaimException e){
            throw new RuntimeException("JWT Token Incorrect ISSUER Exception");
        }
        catch (Exception e) {
            log.error("e={}",e.getMessage());
            throw new RuntimeException("JWT Exception");
        }
    }

    private <T> T getClaimFromToken(String jwsString, Function<Claims, T> claimsFunction){
        Claims claims = validateToken(jwsString);
        return claimsFunction.apply(claims);
    }

    public Integer getUserId(String jwtString){
        return getClaimFromToken(jwtString, claims -> claims.get("user_id",Integer.class));
    }

    public Integer getPartyId(String jwtString){
        return getClaimFromToken(jwtString, claims -> claims.get("party_id",Integer.class));
    }

    public Integer getPartyUserId(String jwtString){
        return getClaimFromToken(jwtString, claims -> claims.get("user_party_id",Integer.class));
    }

    public String getUserNickname(String jwtString){
        return getClaimFromToken(jwtString, claims -> claims.get("user_nickname",String.class));
    }

    public String getUserProfile(String jwtString){
        return getClaimFromToken(jwtString, claims -> claims.get("user_profile",String.class));
    }

    public Date getExpireDate(String jwtString){
        return getClaimFromToken(jwtString, Claims::getExpiration);
    }

}
