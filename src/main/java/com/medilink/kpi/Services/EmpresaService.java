package com.medilink.kpi.Services;

import com.medilink.kpi.entities.Empresa;
import com.medilink.kpi.repositories.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository repository;

    public void save(Empresa empresa){
        repository.save(empresa);
    }

    public List<Empresa> list(){
        return repository.findAll();
    }

    public Empresa findById(int id){
        return repository.findById(id).orElse(null);
    }

    public void delete(){
        repository.deleteAll();
    }

}
