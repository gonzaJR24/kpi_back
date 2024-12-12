package com.medilink.kpi.Services;

import com.medilink.kpi.entities.Sucursal;
import com.medilink.kpi.repositories.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SucursalService {

    @Autowired
    private SucursalRepository repository;

    public void save(Sucursal sucursal){
        repository.save(sucursal);
    }

    public List<Sucursal>list(){
        return repository.findAll();
    }

    public Sucursal findById(int id){
        return repository.findById(id).orElse(null);
    }
}
