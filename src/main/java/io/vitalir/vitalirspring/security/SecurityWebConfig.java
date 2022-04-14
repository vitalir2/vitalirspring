package io.vitalir.vitalirspring.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration
@Profile("!test")
public class SecurityWebConfig extends WebSecurityConfigurerAdapter {

    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;

    public SecurityWebConfig(JwtProvider jwtProvider, UserDetailsService userDetailsService) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .cors().and()
                .authorizeHttpRequests(authorize ->
                        authorize
                                .mvcMatchers("/login").permitAll()
                                .mvcMatchers("/api/v1/services/**").permitAll()
                                .anyRequest().authenticated()
                )
                .userDetailsService(userDetailsService)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .addFilter(new JwtAuthenticationFilter(jwtProvider, authenticationManager()))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), jwtProvider))
                .csrf(csrf -> csrf
                        .ignoringAntMatchers("/login")
                        .ignoringRequestMatchers(request -> request.getHeader("User-Agent").startsWith("Postman"))
                );

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
