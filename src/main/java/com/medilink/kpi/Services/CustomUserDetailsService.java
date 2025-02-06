package com.medilink.kpi.Services;

import com.medilink.kpi.config.CustomUserDetails;
import com.medilink.kpi.entities.Usuario;
import com.medilink.kpi.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = userRepository.findByNombreUsuario(username);
        if (usuario == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(usuario);
    }
}
