package io.vitalir.vitalirspring.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class SecurityWebConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin().and()
                .authorizeHttpRequests( authorize ->
                        authorize
                                .mvcMatchers("/login").permitAll()
                                .mvcMatchers("/api/v1/services/**").permitAll()
                )
                .csrf(csrf -> csrf
                        .ignoringAntMatchers("/login")
                        .ignoringRequestMatchers(request -> request.getHeader("User-Agent").startsWith("Postman"))
                );
    }
}
