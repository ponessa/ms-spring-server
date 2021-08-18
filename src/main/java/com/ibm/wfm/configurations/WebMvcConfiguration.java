package com.ibm.wfm.configurations;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
class WebMvcConfiguration implements WebMvcConfigurer {
    @Override
    public void configureContentNegotiation( ContentNegotiationConfigurer configurer){
        configurer.defaultContentType( MediaType.APPLICATION_JSON );
    }
}