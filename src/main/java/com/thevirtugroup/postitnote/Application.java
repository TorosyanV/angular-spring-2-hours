package com.thevirtugroup.postitnote;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.thevirtugroup.postitnote.config.MvcConfig;
import com.thevirtugroup.postitnote.config.WebSecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 */
@SpringBootApplication
@Import({MvcConfig.class, WebSecurityConfig.class})
@ComponentScan({
        "com.thevirtugroup.postitnote.service",
        "com.thevirtugroup.postitnote.security",
        "com.thevirtugroup.postitnote.data.repository",
        "com.thevirtugroup.postitnote.rest"
})
public class Application extends WebMvcConfigurerAdapter {
    public static void main(String[] args) throws Exception {
        System.setProperty("spring.devtools.restart.enabled", "true");

        SpringApplication.run(Application.class, args);
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }

}
