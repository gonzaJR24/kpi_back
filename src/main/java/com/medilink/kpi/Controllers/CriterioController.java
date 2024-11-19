package com.medilink.kpi.Controllers;

import com.medilink.kpi.Services.AreaService;
import com.medilink.kpi.Services.CriterioService;
import com.medilink.kpi.entities.Area;
import com.medilink.kpi.entities.Criterio;
import com.medilink.kpi.entities.dto.CriterioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/criterio")
@CrossOrigin
public class CriterioController {

    @Autowired
    private CriterioService service;

    @Autowired
    private AreaService areaService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody CriterioDTO criterio){
        Criterio newCriterio=new Criterio();
        if(criterio.nombreCriterio().isEmpty() || criterio.area()==0 || criterio.valor()==0){
            return ResponseEntity.status(400).body("Missing or invalid criteria");
        }

        newCriterio.setNombreCriterio(criterio.nombreCriterio());
        newCriterio.setValor(criterio.valor());

        Area area=areaService.findById(criterio.area());
        newCriterio.setArea(area);
        service.save(newCriterio);
        return ResponseEntity.status(201).body("created successfully");
    }

    @GetMapping
    public List<Criterio> list(){
        return service.list();
    }

}
