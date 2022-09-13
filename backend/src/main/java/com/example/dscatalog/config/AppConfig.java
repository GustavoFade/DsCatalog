package com.example.dscatalog.config;
//classe appConfig vai guardar as configurações do app como um todo

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AppConfig {
//anotation bean é um componente para metodos, dessa forma é possivel injetar eles
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
