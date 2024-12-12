package com.medilink.kpi.Services;

import com.medilink.kpi.entities.Usuario;
import com.medilink.kpi.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    public void save(Usuario usuario){
        repository.save(usuario);
    }

    public List<Usuario>list(){
        return repository.findAll();
    }

    public Usuario findById(int id){
        return repository.findById(id).orElse(null);
    }

    public void deleteAll(){
        repository.deleteAll();
    }

    public void deleteById(int id) {
        Usuario usuario = findById(id);
        if (usuario != null) {
            repository.delete(usuario);
        }
    }
}
