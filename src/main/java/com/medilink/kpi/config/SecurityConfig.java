package com.medilink.kpi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer {

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .cors(cors -> cors.disable())
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/", "/index.html", "/static/**", "/assets/**", "/kpi/**").permitAll() // Permitir acceso a archivos estáticos
        .requestMatchers("/api/users/login", "/api/users/logout", "/login").permitAll()
        .requestMatchers("/api/users").permitAll()
        .requestMatchers("/api/empresa/**").permitAll()
        .anyRequest().permitAll()
      )
      .formLogin(form -> form
        .loginPage("/api/users/login")
        .disable()
      )
      .logout(logout -> logout
        .logoutUrl("/api/users/logout")
        .disable()
      )
      .sessionManagement(session -> session
        .maximumSessions(-1) // Permitir sesiones concurrentes ilimitadas
        .sessionRegistry(new SessionRegistryImpl())
      );

    return http.build();
  }

  @Bean
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
  }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
          .allowedOrigins("http://192.168.4.206:8080")
          .allowedMethods("GET", "POST", "PUT", "DELETE")
          .allowedHeaders("*")
          .allowCredentials(true);
      }

      @Override
      public void addViewControllers(ViewControllerRegistry registry) {
        // Redirige todas las rutas no manejadas a index.html
        registry.addViewController("/kpi/**").setViewName("forward:/index.html");
      }
    };
  }

  @Bean
  public PasswordEncoder encoder(){
    return NoOpPasswordEncoder.getInstance();
  }

}
