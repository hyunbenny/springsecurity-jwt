package com.hyunbenny.springsecurityjwt.config;

import com.hyunbenny.springsecurityjwt.filter.CustomFilter1;
import com.hyunbenny.springsecurityjwt.filter.CustomFilter2;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

//    @Bean
//    public FilterRegistrationBean<CustomFilter1> customFilter1() {
//        FilterRegistrationBean<CustomFilter1> bean = new FilterRegistrationBean<>(new CustomFilter1());
//        bean.addUrlPatterns("/*");
//        bean.setOrder(0); // 0이 가장 높은 우선순위(== 가장 먼저 실행된다.)
//
//        return bean;
//    }

    @Bean
    public FilterRegistrationBean<CustomFilter2> customFilter2() {
        FilterRegistrationBean<CustomFilter2> bean = new FilterRegistrationBean<>(new CustomFilter2());
        bean.addUrlPatterns("/*");
        bean.setOrder(1);

        return bean;
    }
}
