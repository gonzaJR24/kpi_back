package com.medilink.kpi.Services;

import com.medilink.kpi.entities.Criterio;
import com.medilink.kpi.repositories.CriterioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CriterioService {

    @Autowired
    private CriterioRepository repository;

    public void save(Criterio criterios){
        repository.save(criterios);
    }

    public List<Criterio> list(){
        return repository.findAll();
    }

    public void deleteAll(){
        repository.deleteAll();
    }

    public Optional<Criterio> findById(int id){
        return repository.findById(id);
    }

    public void deleteById(int id){
        if(this.findById(id).isPresent()){
            repository.deleteById(id);
        }else{
            throw new EntityNotFoundException("Element not found");
        }
    }
}
