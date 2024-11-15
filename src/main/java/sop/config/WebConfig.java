package sop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import sop.interceptor.RoleInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

//    @Autowired
//    private RoleInterceptor roleBasedInterceptor;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(roleBasedInterceptor)
//                .addPathPatterns("/**"); 
//    }
}
