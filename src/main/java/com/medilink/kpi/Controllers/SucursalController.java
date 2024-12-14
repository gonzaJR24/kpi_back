package com.medilink.kpi.Controllers;

import com.medilink.kpi.Services.EmpresaService;
import com.medilink.kpi.Services.SucursalService;
import com.medilink.kpi.entities.Sucursal;
import com.medilink.kpi.entities.dto.SucursalDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sucursal")
public class SucursalController {

    @Autowired
    private SucursalService sucursalService;

    @Autowired
    private EmpresaService empresaService;

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody SucursalDTO sucursalDTO){
        if(sucursalDTO.nombreSucursal().isEmpty()|| sucursalDTO.empresa()==0){
            return ResponseEntity.status(400).body("Cannot be empty");
        }

        Sucursal sucursal=new Sucursal();
        sucursal.setNombreSucursal(sucursalDTO.nombreSucursal());
        sucursal.setEmpresa(empresaService.findById(sucursalDTO.empresa()));
        sucursalService.save(sucursal);

        return ResponseEntity.status(201).body("created successfully");
    }

    @GetMapping
    public List<Sucursal>list(){
        return sucursalService.list();
    }
}
