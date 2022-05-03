package io.vitalir.vitalirspring.security;

import io.vitalir.vitalirspring.common.constants.HttpEndpoints;
import io.vitalir.vitalirspring.common.properties.PropertiesSecurityConfig;
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
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
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

    private final PropertiesSecurityConfig config;

    public SecurityWebConfig(
            UserDetailsService userDetailsService,
            JwtProvider jwtProvider, JwtVerifier jwtVerifier,
            PasswordEncoder passwordEncoder,
            PropertiesSecurityConfig config
    ) {
        this.userDetailsService = userDetailsService;
        this.jwtProvider = jwtProvider;
        this.jwtVerifier = jwtVerifier;
        this.passwordEncoder = passwordEncoder;
        this.config = config;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        configureAuthorization(http);
        configureCsrf(http);
        configureMiscellaneous(http);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().mvcMatchers("/api/v1/auth/**");
    }

    private void configureAuthorization(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> {
                    authorize
                            .mvcMatchers("/api/v1/auth/**").permitAll()
                            .mvcMatchers("/api/v1/register/**").permitAll()
                            .mvcMatchers(HttpMethod.GET, "/api/v1/services/**").permitAll()
                            .mvcMatchers("/api/v1/services/**").hasRole(Role.ADMIN.name())
                            .mvcMatchers("/api/v1/appointments/**").hasRole(Role.USER.name())
                            .mvcMatchers(HttpMethod.GET, "/api/v1/doctors/**").permitAll()
                            .mvcMatchers("/api/v1/doctors/**").hasRole(Role.ADMIN.name());
                    configureSwaggerAuth(authorize);
                    configureActuatorAuth(authorize);
                    authorize.anyRequest().authenticated();
                }
        );
    }

    private void configureSwaggerAuth(
            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorize
    ) {
        for (String endpoint : HttpEndpoints.SWAGGER_ENDPOINTS) {
            if (config.isSwaggerSecurityEnabled()) {
                authorize.mvcMatchers(endpoint).hasRole(Role.ADMIN.name());
            } else {
                authorize.mvcMatchers(endpoint).permitAll();
            }
        }
    }

    private void configureActuatorAuth(
            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorize
    ) {
        if (config.isActuatorSecurityEnabled()) {
            authorize.mvcMatchers(HttpEndpoints.SPRING_ACTUATOR_PATTERN).hasRole(Role.ADMIN.name());
        } else {
            authorize.mvcMatchers(HttpEndpoints.SPRING_ACTUATOR_PATTERN).permitAll();
        }
    }

    private void configureCsrf(HttpSecurity http) throws Exception {
        if (config.isCsrfEnabled()) {
            http.csrf(csrf -> csrf
                    .ignoringRequestMatchers(request -> {
                        var userAgent = request.getHeader(HttpHeaders.USER_AGENT);
                        return userAgent != null && userAgent.startsWith("Postman");
                    })
            );
        } else {
            http.csrf().disable();
        }
    }

    private void configureMiscellaneous(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .formLogin().disable()
                .cors().and()
                .userDetailsService(userDetailsService)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .addFilterBefore(new JwtFilter(jwtProvider, jwtVerifier), UsernamePasswordAuthenticationFilter.class);
    }
}
