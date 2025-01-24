package com.upload.upload.Config;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;
  private final String SECRET_KEY = "una_clave_secreta_muy_segura_de_al_menos_32_caracteres"; // Use a secure secret key

  public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
    return authenticationManager.authenticate(authToken);
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                          Authentication authResult) throws IOException, ServletException {

    Collection<? extends GrantedAuthority>authorities=authResult.getAuthorities();
    String roles=authorities.stream() .map(GrantedAuthority::getAuthority)
      .collect(Collectors.joining(","));

    String token = Jwts.builder()
      .setSubject(((UserDetails) authResult.getPrincipal()).getUsername())
      .claim("roles", "ROLE_ADMIN")
      .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 30sec
      .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
      .compact();
    response.addHeader("Authorization", "Bearer " + token);
  }
}
