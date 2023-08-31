package com.hyunbenny.springsecurityjwt.config;

import com.hyunbenny.springsecurityjwt.filter.CustomFilter1;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
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
         * 단, 시큐리티 필터가 먼저 동작하고 내가 만든 필터가 동작한다.
         * 시큐리티 필터보다 먼저 동작하게 하고 싶으면, 
         * addFilterBefore()로 시큐리티 필터 중 가장 먼저 동작하는 SecurityContextPersistenceFilter 앞에 필터를 추가해야 한다.
         */
        http.addFilter(corsFilter);
//        http.addFilter(new CustomFilter1()); // Filter는 Security Filter에 순서를 정하지 않고 등록할 수 없다. (-> addBefore(), addAfter()를 써야 한다.)
//        http.addFilterBefore(new CustomFilter1(), BasicAuthenticationFilter.class); // 굳이 시큐리티 필터에 걸 필요 없이 FilterConfig를 따로 만들어서 걸어보자.

        http.addFilterBefore(new CustomFilter1(), SecurityContextPersistenceFilter.class);


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
