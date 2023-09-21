package com.bloom.pium.config.security;

import com.bloom.pium.data.UserRoleEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret.key}")
    private String key = "secretkey";

    //private Key secretKey;

    private final long tokenExp = 1000L * 60 * 60; // 만료시간 : 1Hour

    private final UserDetailsService userDetailsService;

    @PostConstruct
    protected void init(){
        key =  Base64.getEncoder().encodeToString(key.getBytes(StandardCharsets.UTF_8));
     //secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(String username, List<UserRoleEnum> roles){
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles",roles);
        Date now = new Date();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+tokenExp))
                .signWith(SignatureAlgorithm.HS256,key) //.signWith(SignatureAlgorithm.PS256,key)
                .compact();
        return token;
    }

    // 권한정보
    public Authentication getAuthentication(String token){
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());
    }

    private String getUsername(String token) {
        String info = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
        return  info;
    }

    // Header를 통해 인증을 한다.
    public String resolveToken(HttpServletRequest request){
        return request.getHeader("Authorization"); //프론트 "Authorization": 클라이언트가 요청을 보낼 때, HTTP 요청 헤더의 Authorization 필드
    }

    //토큰 유효 체크,검증
    public boolean validateToken(String token){
        try{
            if(!token.substring(0,"BEARER".length()).equalsIgnoreCase("BARER")){
                return false;
                //Bearer 검증은 JWT 토큰을 HTTP 요청의 Authorization 헤더에서 추출할 때, 해당 헤더 값이 "Bearer "로 시작하는지 확인하는 과정을 말합니다.
            }else {
                token = token.split(" ")[1].trim();
                //"Bearer " 다음에 오는 실제 JWT 토큰을 추출

            }
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date()); // 만료 false, 유효 true
        }catch (Exception e){
            return  false;
        }
    }



}
