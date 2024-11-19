package com.medilink.kpi.Services;

import com.medilink.kpi.entities.Cargo;
import com.medilink.kpi.repositories.CargoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CargoService {
    @PersistenceContext
    private EntityManager entityManager;

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

    @Transactional
    public void deleteAll(){
        repository.deleteAll();
    }

    public void deleteCargo(int id){
        if(this.findById(id)!=null){
            repository.deleteById(id);
        }else{
            throw new EntityNotFoundException("Element not found");
        }
    }
    
}
