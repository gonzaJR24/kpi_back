package com.medilink.kpi.Services;


import com.medilink.kpi.entities.Usuario;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionService {
    private final ConcurrentHashMap<String, Usuario> sessionStore = new ConcurrentHashMap<>();

    public void storeSession(String token, Usuario usuario) {
        sessionStore.put(token, usuario);
    }

    public Usuario getSession(String token) {
        return sessionStore.get(token);
    }

    public boolean removeSession(String token) {
        return sessionStore.remove(token) != null;
    }
}
