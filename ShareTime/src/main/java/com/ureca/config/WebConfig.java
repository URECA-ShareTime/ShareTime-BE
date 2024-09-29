package com.ureca.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 엔드포인트에 대해 CORS 설정
                .allowedOrigins("http://localhost:5173") // 허용할 출처 (프론트엔드 주소)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메소드
                .allowedHeaders("*") // 허용할 헤더
                .allowCredentials(true); // 자격 증명 (쿠키 등)을 허용할지 여부
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 실제 파일 경로와 URL 경로를 매핑
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:/Users/HYERYEONG/git/ShareTime-BE/ShareTime/uploads/");
    }
}