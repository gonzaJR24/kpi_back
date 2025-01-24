package com.upload.upload.services;

import com.upload.upload.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;


@Service
public class CustomDetailsService implements UserDetailsService {

  @Autowired
  private UserService userService; // Your service to fetch UserEntity

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity userEntity = userService.findByUsername(username);
    if (userEntity == null) {
      throw new UsernameNotFoundException("User not found");
    }
    System.out.println("User found: " + userEntity.getUsername()); // Log the found user
    return new org.springframework.security.core.userdetails.User(
      userEntity.getUsername(),
      userEntity.getPassword(), // Ensure this is correct
      getAuthority(userEntity)
    );
  }

  private Collection<? extends GrantedAuthority> getAuthority(UserEntity userEntity) {
    String role = userEntity.getRole(); // Get the role as a string
    return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.trim())); // Ensure it starts with "ROLE_"
  }

}

