package com.upload.upload.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class JwtService {

  @Autowired
  private UserService userService;



  private String secretKey="una_clave_secreta_muy_segura_de_al_menos_32_caracteres";

  private int expiration= 3600000;

  private SecretKey getKey() {
    return Keys.hmacShaKeyFor(secretKey.getBytes());
  }

  public String createToken(Authentication authentication) {
    String username = authentication.getName();

    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    // Convert roles to a list of strings
    String roles = authorities.stream()
      .map(GrantedAuthority::getAuthority)
      .collect(Collectors.joining(","));


    return Jwts.builder()
      .setSubject(userService.findByUsername(username).getName())
      .claim("roles", roles)
      .setExpiration(new Date(System.currentTimeMillis() + expiration))
      .signWith(getKey(), SignatureAlgorithm.HS256)
      .compact();
  }

  public Claims getClaims(String token) {
    return Jwts.parser()
      .setSigningKey(getKey())
      .build()
      .parseClaimsJws(token)
      .getBody();
  }

  public Collection<? extends GrantedAuthority> getRolesFromToken(String token) {
    Claims claims = getClaims(token); // Extract claims from the token
    String roles = claims.get("roles", String.class); // Get the roles from the claims as a string
    // Split roles into a collection of GrantedAuthority
    return Arrays.stream(roles.split(",")) // Split by comma to get individual roles
      .map(SimpleGrantedAuthority::new) // Correctly map each role to a SimpleGrantedAuthority
      .collect(Collectors.toList()); // Collect into a list of GrantedAuthority objects
  }

}


