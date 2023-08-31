package com.hyunbenny.springsecurityjwt.config;

import org.springframework.web.filter.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowCredentials(true); // 서버에서 응답값을 보내줄 때, 자바스크립트에서 응답값을 받아서 처리할 수 있도록 true로 설정
        corsConfiguration.addAllowedOrigin("*"); // "*" : 모든 아이피에 대해서 응답을 허용하겠다.
        corsConfiguration.addAllowedHeader("*"); // "*" : 모든 헤더에 대해서 응답을 허용하겠다.
        corsConfiguration.addAllowedMethod("*"); // "*" : 모든 http method 대해서 응답을 허용하겠다.

        urlBasedCorsConfigurationSource.registerCorsConfiguration("/api/**", corsConfiguration);

        return new CorsFilter(urlBasedCorsConfigurationSource);
    }
}
