package dev.fmhj97.coursesplatform.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // Marks this class as a Spring configuration class
@EnableWebSecurity // Enables Spring Security in the application
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    /**
     * Constructor
     * @param jwtAuthFilter
     */
    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    /**
     * Configures the security filter chain, defining which endpoints are public
     * and which require authentication or specific roles.
     * @param http the HttpSecurity object to configure
     * @return the configured SecurityFilterChain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Disable CSRF — not needed for stateless REST APIs with JWT
                .csrf(csrf -> csrf.disable())

                // Use stateless sessions — no HTTP session, each request is independent
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Define access rules for each endpoint
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/courses/**").hasAnyRole("INSTRUCTOR", "ADMIN", "STUDENT")
                        .requestMatchers("/instructors/**").hasAnyRole("INSTRUCTOR", "ADMIN")
                        .requestMatchers("/students/**").hasAnyRole("INSTRUCTOR", "ADMIN", "STUDENT")
                        .requestMatchers("/lessons/**").hasAnyRole("INSTRUCTOR", "ADMIN", "STUDENT")
                        .requestMatchers("/enrollments/**").hasAnyRole("INSTRUCTOR", "ADMIN", "STUDENT")
                        .anyRequest().authenticated()
                )

                // Handle authentication errors (invalid credentials) — returns 401 instead of default 403
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"Invalid credentials\"}");
                        })
                )

                // Register JwtAuthFilter before Spring's default authentication filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configures the password encoder using BCrypt hashing algorithm.
     * @return a BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the authentication manager using the UserDetailsService and password encoder (AuthController).
     * @param config the AuthenticationConfiguration
     * @return the configured AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
