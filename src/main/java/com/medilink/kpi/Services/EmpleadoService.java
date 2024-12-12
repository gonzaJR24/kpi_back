package com.medilink.kpi.Services;

import com.medilink.kpi.entities.Area;
import com.medilink.kpi.entities.Empleado;
import com.medilink.kpi.repositories.EmpleadoRepository;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

}

