package com.upload.upload.Config;

import java.io.IOException;
import java.util.Date;

import com.upload.upload.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private JwtService jwtService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {

    if (SecurityContextHolder.getContext().getAuthentication() == null) {

      // Get the Authorization header from the request
      final String authorization = request.getHeader("Authorization");

      // If the Authorization header is present and starts with "Bearer"
      if (authorization != null && authorization.startsWith("Bearer ")) {

        // Extract the token from the Authorization header
        final String token = authorization.substring(7);

        // Get claims from the token
        final Claims claims = jwtService.getClaims(token);

        // Check if the token is not expired
        if (claims.getExpiration().after(new Date())) {

          // Extract username from the claims
          final String username = claims.getSubject();

          // Load user details (make sure this user exists in your UserDetailsService)
          final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

          // Create the authentication token (no credentials, authorities are taken from the token)
          final UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(
              userDetails, null, userDetails.getAuthorities());

          // Set additional details (e.g., IP address, session info, etc.)
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

          // Set the authentication token into the SecurityContext
          SecurityContextHolder.getContext().setAuthentication(authToken);
        }
      }
    }

    // Proceed with the filter chain (allow the request to continue)
    filterChain.doFilter(request, response);
  }
}

