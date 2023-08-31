package com.hyunbenny.springsecurityjwt.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyunbenny.springsecurityjwt.auth.PrincipalDetails;
import com.hyunbenny.springsecurityjwt.controller.request.LoginRequest;
import com.hyunbenny.springsecurityjwt.domain.UserAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

/**
 * UsernamePasswordAuthenticationFilter는 formLogin을 사용할 때, POST로 /login요청이 오면 동작한다.
 * UsernamePasswordAuthenticationFilter을 상속해서 SecurityConfig에 등록해주면 된다.
 */

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static int JWT_EXPIRATION_TIME = (60000 * 10);
    private static String JWT_SECRET = "HYUNBENNY";



    private final AuthenticationManager authenticationManager;
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * `/login`요청을 하면 로그인 시도를 위해 실행되는 메서드
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("JwtAuthenticationFilter - attemptAuthentication()");

        // username, password를 받아서 인증 처리(AuthenticationManager로 실행하면 PrincipalDetailsService가 호출되고
        // loadUserByUsername()를 통해 인증을 진행한다. -> PrincipalDetails를 Session에 저장
        // 굳이 Session에 담는 이유는 권한 관리를 위해서

        try {
            // 요청 데이터 받아오기 -> 이거 말고 밑에 방식을 쓰자.
//            BufferedReader reader = request.getReader();
//            String input = "";
//
//            while ((input = reader.readLine()) != null) {
//                log.info("input: {}", input);
//            }

            // 요청 데이터 받아오기
            LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
            log.info(loginRequest.toString());

            // 로그인 시도
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());

            // PrincipalDetailsService - loadByUsername() 실행된다. -> Authentication에 로그인한 정보가 담긴다.
            Authentication auth = authenticationManager.authenticate(token);
            PrincipalDetails principalDetails = (PrincipalDetails) auth.getPrincipal();
            log.info("login success -> principalDetails.getUserAccount(): {}", principalDetails.getUserAccount().toString());
            // -> authentication객체가 session에 저장됨
            return auth;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * attemptAuthentication()가 종료되면 successfulAuthentication()가 호출된다.
     * 여기서 JWT 토큰을 만들어서 응답해주면 된다.
     * <p>
     * 따라서 attemptAuthentication()은 인증처리만 담당하면 된다.
     * 굳이 attemptAuthentication()에서 토큰 생성과 리턴까지 할 필요가 없다.
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        String jwtToken = JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JWT_EXPIRATION_TIME))
                .withClaim("id", principalDetails.getUserAccount().getId())
                .withClaim("username", principalDetails.getUserAccount().getUsername())
                .sign(Algorithm.HMAC256(JWT_SECRET)); // RSA방식이 아니라 Hash방식(HMAC256은 서버만 SECRET 키를 가지고 있으면 된다.)


        log.info("jwtToken: {}", jwtToken);
        String bearerToken = "Bearer " + jwtToken;

        response.addHeader("Authorization", bearerToken);
    }


}
