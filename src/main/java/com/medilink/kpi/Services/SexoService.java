package com.medilink.kpi.Services;

import com.medilink.kpi.entities.Sexo;
import com.medilink.kpi.repositories.SexoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SexoService {

    @Autowired
    private SexoRepository repository;

    public void save(Sexo sexo){
        repository.save(sexo);
    }

    public List<Sexo> list(){
        return repository.findAll();
    }

    public Sexo findById(int id){
        return repository.findById(id).orElse(null);
    }
}
