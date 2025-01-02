package com.medilink.kpi.Controllers;

import com.medilink.kpi.Services.CargoService;
//import com.medilink.kpi.Services.EmpleadoService;
import com.medilink.kpi.Services.EmpleadoService;
import com.medilink.kpi.Services.PresupuestoService;
import com.medilink.kpi.entities.Cargo;
import com.medilink.kpi.entities.Empleado;
import com.medilink.kpi.entities.Presupuesto;
import com.medilink.kpi.entities.dto.CargoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cargo")
@CrossOrigin
public class CargoController {
    @Autowired
    private EmpleadoService empleadoService;
    @Autowired
    private PresupuestoService presupuestoService;
    @Autowired
    private CargoService cargoService;
    @PostMapping
    public ResponseEntity<?> save(@RequestBody CargoDTO cargoDTO) {
        Cargo cargo = new Cargo();
        if (cargoDTO.nombreCargo().isEmpty()) {
            return ResponseEntity.status(400).body("Cannot be empty");
        }
        cargo.setNombreCargo(cargoDTO.nombreCargo());
        setPresupuesto();
        cargoService.save(cargo);
        return ResponseEntity.status(201).body(cargo);
    }
    @GetMapping
    public List<Cargo> list() {
        setPresupuesto();
        return cargoService.list();
    }

    public void setPresupuesto() {
        List<Presupuesto> presupuestos = presupuestoService.list();
        for (Cargo cargo : cargoService.list()) {
            Presupuesto ultimo_presupuesto = presupuestos.get(presupuestos.size() - 1);
            cargo.setPresupuesto(ultimo_presupuesto);
        }
    }
}