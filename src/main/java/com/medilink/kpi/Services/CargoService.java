package com.medilink.kpi.Services;

import com.medilink.kpi.entities.Cargo;
import com.medilink.kpi.repositories.CargoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CargoService {

    @Autowired
    private CargoRepository repository;

    public void save(Cargo cargo){
        repository.save(cargo);
    }

    public List<Cargo>list(){
        return repository.findAll();
    }

    public Cargo findById(int id){
        return repository.findById(id).orElse(null);
    }
}
