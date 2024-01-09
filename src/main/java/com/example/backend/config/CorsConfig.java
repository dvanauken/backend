//package com.example.backend.config;
//
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class CorsConfig implements WebMvcConfigurer {
////    @Override
////    public void addCorsMappings(CorsRegistry registry) {
////        registry.addMapping("/**")
////                .allowedOrigins("http://localhost:4200")
////                .allowedMethods("GET", "POST", "PUT", "DELETE")
////                .allowedHeaders("*")
////                .allowCredentials(true)
////                .maxAge(3600);
////    }
////
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("*") // Allow any origin
//                .allowedMethods("*") // Allow all methods
//                .allowedHeaders("*") // Allow all headers
//                .allowCredentials(true) // Allow credentials
//                .maxAge(3600); // Set max age
//    }
//
//}
//
//
