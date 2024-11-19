package com.medilink.kpi.Controllers;

import com.medilink.kpi.Services.SexoService;
import com.medilink.kpi.entities.Sexo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sexo")
@CrossOrigin
public class SexoController {

    @Autowired
    private SexoService service;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Sexo sexo){
        if(sexo.getSexo().isEmpty()){
            return ResponseEntity.status(400).body("Cannot be empty");
        }
        service.save(sexo);
        return ResponseEntity.status(201).body("created successfully");
    }

    @GetMapping
    public List<Sexo>list(){
        return service.list();
    }
}
