package com.hyunbenny.springsecurityjwt.filter;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class CustomFilter1 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("======================customFilter1======================");

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if (httpServletRequest.getMethod().equals(HttpMethod.POST.name())) {
            String authorization = httpServletRequest.getHeader("Authorization");
            log.info("auth : {}", authorization);
            if (authorization.equals("hello")) {
                log.info("인증 성공");
                chain.doFilter(request, response);
            } else {
                log.info("인증 실패");
                PrintWriter writer = response.getWriter();
                writer.write("인증 안된 사용자");
                writer.flush();
                writer.close();
            }
            
        }
    }
}
