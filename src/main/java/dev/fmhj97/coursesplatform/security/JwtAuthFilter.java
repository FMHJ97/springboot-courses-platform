package dev.fmhj97.coursesplatform.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Constructor
     * @param jwtService
     * @param userDetailsService
     */
    public JwtAuthFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // Read Authorization header from the request.
        final String authHeader = request.getHeader("Authorization");

        // If there is no token or it doesn't start with "Bearer ", pass the request without authenticating.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the token by removing the "Bearer " prefix (7 characters).
        final String token = authHeader.substring(7);

        // Extract the email from the token payload.
        final String email = jwtService.extractUsername(token);

        // If the email is valid and the user is not already authenticated in this request.
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Load the user from the database using the email.
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // If the token is valid for this user.
            if (jwtService.isTokenValid(token, userDetails)) {

                // Create an authentication object with the user's details and authorities.
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );

                // Attach request details to the authentication object.
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // Register the authentication in Spring Security's context for this request.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Pass the request to the next filter or controller.
        filterChain.doFilter(request, response);
    }
}
