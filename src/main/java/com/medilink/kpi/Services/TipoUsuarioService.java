package com.medilink.kpi.Services;

import com.medilink.kpi.entities.TipoUsuario;
import com.medilink.kpi.repositories.TipoUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoUsuarioService {

    @Autowired
    private TipoUsuarioRepository repository;

    public void save(TipoUsuario tipoUsuario){
        repository.save(tipoUsuario);
    }

    public List<TipoUsuario> list(){
        return repository.findAll();
    }

    public TipoUsuario findById(int id){
        return repository.findById(id).orElse(null);
    }
}
