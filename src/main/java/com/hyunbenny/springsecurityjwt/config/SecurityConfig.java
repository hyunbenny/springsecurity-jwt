package com.hyunbenny.springsecurityjwt.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        /**
         * Controller에 @CrossOrigin을 붙이는 방법은
         * 인증이 필요없는 요청은 동작하지만 인증이 필요한 요청은 다 막아버리기 때문에
         * 우리의 의도와 다르게 동작하게 된다.
         *
         * 따라서 필터를 만들어서 시큐리티 필터에 등록해주는 방법을 사용한다.
         */
        http.addFilter(corsFilter);

        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .formLogin().disable()
                .httpBasic().disable()

                .authorizeRequests()
                .antMatchers("/api/v1/user/**").access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/manager/**").access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
        ;
    }
}
