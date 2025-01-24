package com.upload.upload.controllers;

import com.upload.upload.entities.LoginDTO;
import com.upload.upload.entities.UserDTO;
import com.upload.upload.entities.UserEntity;
import com.upload.upload.services.AuthorizationService;
import com.upload.upload.services.CustomDetailsService;
import com.upload.upload.services.JwtService;
import com.upload.upload.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/user")
public class UserController {
  public String token;
  public Authentication authentication;

  @Autowired
  private UserService service;

  @Autowired
  private AuthorizationService authorizationService;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private CustomDetailsService customDetailsService;

  @Autowired
  private JwtService jwtService;

  @PostMapping("/agregar_usuario")
  public ResponseEntity<?> save(@RequestBody UserEntity user){
    UserEntity existing=service.findByUsername(user.getUsername());
    if(existing==null){
    service.save(user);
    return ResponseEntity.status(200).body(user);
    }
    return ResponseEntity.status(400).body("User already exist");
  }

  @GetMapping
  public List<UserEntity> list(){
    return service.list();
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
    try {
      authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
          loginDTO.username(),
          loginDTO.password()
        )
      );
      SecurityContextHolder.getContext().setAuthentication(authentication);

      token = jwtService.createToken(authentication); // Your method to create a token


      return ResponseEntity.ok().body(Collections.singletonMap("token", token));
    } catch (BadCredentialsException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authentication);
    }
  }

  @PutMapping("/edit")
  public ResponseEntity<?> editUser(@RequestBody UserDTO userDTO) {
    UserEntity existingUser = service.findById(userDTO.id());
    if (existingUser != null) {
      // Actualizar los campos del usuario
      existingUser.setUsername(userDTO.username());
      existingUser.setPassword(userDTO.password());
      existingUser.setRole(userDTO.role());

      service.save(existingUser); // Guardar los cambios
      return ResponseEntity.ok(existingUser);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
  }

  @DeleteMapping("{id}")
  public void deleteById(@PathVariable int id){
    service.deleteById(id);
  }

}
