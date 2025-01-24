package com.upload.upload.services;

import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

  public boolean hasAccess(String role, String url) {
    // Lógica para determinar si el rol tiene acceso a la URL
    if (role.equals("ADMIN")) {
      return true;
    }
    return false;
  }
}

