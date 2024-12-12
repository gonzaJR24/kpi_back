package com.medilink.kpi.Services;

import com.medilink.kpi.entities.Puntaje;
import com.medilink.kpi.repositories.PuntajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PuntajeService {

    @Autowired
    private PuntajeRepository repository;

    public void save(Puntaje puntaje){
        repository.save(puntaje);
    }

    public List<Puntaje> list(){
        return repository.findAll();
    }
}
