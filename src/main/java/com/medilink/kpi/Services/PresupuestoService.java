package com.medilink.kpi.Services;

import com.medilink.kpi.entities.Presupuesto;
import com.medilink.kpi.repositories.PresupuestoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PresupuestoService {
    @Autowired
    private PresupuestoRepository repository;

    public void save(Presupuesto presupuesto){
        repository.save(presupuesto);
    }

    public List<Presupuesto>list(){
        return repository.findAll();
    }

    public void delete(){
        repository.deleteAll();
    }
}
