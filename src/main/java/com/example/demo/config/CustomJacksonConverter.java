package com.example.demo.config;

import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomJacksonConverter {


    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            SimpleFilterProvider filterProvider = new SimpleFilterProvider();

            filterProvider.addFilter("restDataFilter", SimpleBeanPropertyFilter.serializeAllExcept());

            builder.filters(filterProvider);
        };
    }

}