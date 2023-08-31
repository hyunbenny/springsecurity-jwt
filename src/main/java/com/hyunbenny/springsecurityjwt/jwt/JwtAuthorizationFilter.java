package com.hyunbenny.springsecurityjwt.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hyunbenny.springsecurityjwt.auth.PrincipalDetails;
import com.hyunbenny.springsecurityjwt.domain.UserAccount;
import com.hyunbenny.springsecurityjwt.repository.UserAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 권한 체크
 * BasicAuthenticationFilter
 *
 * 권한이나 인증이 필요한 요청인 경우, BasicAuthenticationFilter가 동작한다.
 * (권한이나 인증이 필요없는 요청인 경우, BasicAuthenticationFilter는 동작하지 않는다.)
 */

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private static String JWT_SECRET = "HYUNBENNY";
    private UserAccountRepository userAccountRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserAccountRepository userAccountRepository) {
        super(authenticationManager);
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("============== JwtAuthorizationFilter ==============");

        String getAuthorization = request.getHeader("Authorization");
        log.info("request header Authorization: {}", getAuthorization);
        
        // 토큰 검증 시작
        if (getAuthorization == null || !getAuthorization.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        // 정상 토큰인 경우 사용자 검증 시작
        String token = getAuthorization.split(" ")[1];
        log.info("token: {}", token);

        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(JWT_SECRET)).build().verify(token);
        Integer claimId = decodedJWT.getClaim("id").asInt();
        String claimUsername = decodedJWT.getClaim("username").asString();
        log.info("claimId: {}, claimUsername: {}", claimId, claimUsername);

        if (claimUsername != null) {
            UserAccount userAccount = userAccountRepository.findByUsername(claimUsername).orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다. username: " + claimUsername));
            PrincipalDetails principalDetails = new PrincipalDetails(userAccount);

            // 로그인하면 자동으로 생성되는 Authentication객체가 아니라 Token 서명을 통해 정상인 경우 Authentication객체를 강제로 만들어준다.
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }
}
