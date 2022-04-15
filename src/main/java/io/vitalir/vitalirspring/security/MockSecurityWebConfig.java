package io.vitalir.vitalirspring.security;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;

@TestConfiguration
public class MockSecurityWebConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(
                        User.withUsername("admin").password("1234").roles("ADMIN")
                )
                .withUser(
                        User.withUsername("vitalir").password("2345").roles("USER")
                );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    }
}
