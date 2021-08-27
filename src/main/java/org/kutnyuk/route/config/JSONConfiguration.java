package org.kutnyuk.route.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class JSONConfiguration {
    @Bean
    public ObjectMapper getObjectMapper(){
        return new ObjectMapper();
    }
}
