package io.vitalir.vitalirspring.security;

import io.vitalir.vitalirspring.features.user.domain.model.Role;
import io.vitalir.vitalirspring.security.jwt.JwtFilter;
import io.vitalir.vitalirspring.security.jwt.JwtProvider;
import io.vitalir.vitalirspring.security.jwt.JwtVerifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@Profile({"prod", "test-security"})
public class SecurityWebConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final JwtProvider jwtProvider;
    private final JwtVerifier jwtVerifier;
    private final PasswordEncoder passwordEncoder;

    public SecurityWebConfig(UserDetailsService userDetailsService, JwtProvider jwtProvider, JwtVerifier jwtVerifier, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.jwtProvider = jwtProvider;
        this.jwtVerifier = jwtVerifier;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .formLogin().disable()
                .cors().and()
                .authorizeHttpRequests(authorize ->
                        authorize
                                .mvcMatchers("/api/v1/auth/**").permitAll()
                                .mvcMatchers("/api/v1/register/**").permitAll()
                                .mvcMatchers(HttpMethod.GET, "/api/v1/services/**").permitAll()
                                .mvcMatchers( "/api/v1/services/**").hasRole(Role.ADMIN.name())
                                .mvcMatchers("/swagger-ui.html").hasRole(Role.ADMIN.name())
                                .anyRequest().authenticated()
                )
                .userDetailsService(userDetailsService)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .addFilterBefore(new JwtFilter(jwtProvider, jwtVerifier), UsernamePasswordAuthenticationFilter.class)
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(request -> {
                            var userAgent = request.getHeader(HttpHeaders.USER_AGENT);
                            return userAgent != null && userAgent.startsWith("Postman");
                        })
                );

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().mvcMatchers("/api/v1/auth/**");
    }
}
