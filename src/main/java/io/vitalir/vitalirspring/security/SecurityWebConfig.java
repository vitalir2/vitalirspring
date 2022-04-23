package io.vitalir.vitalirspring.security;

import io.vitalir.vitalirspring.common.HttpEndpoints;
import io.vitalir.vitalirspring.features.user.domain.model.Role;
import io.vitalir.vitalirspring.security.jwt.JwtFilter;
import io.vitalir.vitalirspring.security.jwt.JwtProvider;
import io.vitalir.vitalirspring.security.jwt.JwtVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

import java.util.List;

@EnableWebSecurity
@Configuration
@Profile({"prod", "test-security"})
public class SecurityWebConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final JwtProvider jwtProvider;
    private final JwtVerifier jwtVerifier;
    private final PasswordEncoder passwordEncoder;

    private final boolean enableSwaggerSecurity;

    public SecurityWebConfig(
            UserDetailsService userDetailsService,
            JwtProvider jwtProvider, JwtVerifier jwtVerifier,
            PasswordEncoder passwordEncoder,
            @Qualifier("enable-swagger-security")
            boolean enableSwaggerSecurity
    ) {
        this.userDetailsService = userDetailsService;
        this.jwtProvider = jwtProvider;
        this.jwtVerifier = jwtVerifier;
        this.passwordEncoder = passwordEncoder;
        this.enableSwaggerSecurity = enableSwaggerSecurity;
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
                            .mvcMatchers("/api/v1/services/**").hasRole(Role.ADMIN.name());
                    configureSwaggerAuth(authorize);
                    authorize.anyRequest().authenticated();
                }
        );
    }

    private void configureSwaggerAuth(
            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorize
    ) {
        for (String endpoint: HttpEndpoints.SWAGGER_ENDPOINTS) {
            if (enableSwaggerSecurity) {
                authorize.mvcMatchers(endpoint).hasRole(Role.ADMIN.name());
            } else {
                authorize.mvcMatchers(endpoint).permitAll();
            }
        }
    }

    private void configureCsrf(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf
                .ignoringRequestMatchers(request -> {
                    var userAgent = request.getHeader(HttpHeaders.USER_AGENT);
                    return userAgent != null && userAgent.startsWith("Postman");
                })
        );
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
