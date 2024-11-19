package com.medilink.kpi.Services;

import com.medilink.kpi.entities.Area;
import com.medilink.kpi.repositories.AreaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AreaService {
    @Autowired
    private AreaRepository repository;

    @Autowired
    private EmpleadoService empleadoService;

    public void save(Area area) {
        repository.save(area);
    }

    public List<Area> list(){
        return repository.findAll();
    }

    public Area findById(int id){
        return repository.findById(id).orElse(null);
    }

    public void deleteAll(){
        repository.deleteAll();
    }

    public void deleteById(int id) throws Exception {
        if(this.findById(id)!=null){
            repository.deleteById(id);
        }else{
            throw new EntityNotFoundException("Element not found");
        }
    }
}
