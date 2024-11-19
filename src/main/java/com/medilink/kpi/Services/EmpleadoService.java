package com.medilink.kpi.Services;

import com.medilink.kpi.entities.Empleado;
import com.medilink.kpi.repositories.EmpleadoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoService {


    @Autowired
    private EmpleadoRepository repository;

    public void save(Empleado empleado) {
        repository.save(empleado);
    }

    public List<Empleado> list() {
        return repository.findAll();
    }

    public Empleado findById(int id){
        return repository.findById(id).orElse(null);
    }

    public void deleteAll(){
        repository.deleteAll();
    }

    public void deleteById(int id){
        if(this.findById(id)!=null){
            repository.deleteById(id);
        }else{
            throw new EntityNotFoundException("ELement not found");
        }
    }

}

