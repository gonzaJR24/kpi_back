package com.medilink.kpi.Controllers;

import com.medilink.kpi.Services.CargoService;
import com.medilink.kpi.Services.EmpresaService;
import com.medilink.kpi.Services.PresupuestoService;
import com.medilink.kpi.entities.Presupuesto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/presupuesto")
@CrossOrigin
public class PresupuestoController {

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private CargoService cargoService;

    @Autowired
    private PresupuestoService presupuestoService;

    @GetMapping
    public List<Presupuesto> list(){
        return presupuestoService.list();
    }

    @DeleteMapping
    public ResponseEntity<?> delete(){
        presupuestoService.delete();
        return ResponseEntity.status(200).body("Deleted successfully");
    }

    @GetMapping("/ultimoPresupuesto")
    public ResponseEntity<?>ultimoPresupuesto(){
        List<Presupuesto> presupuestos = presupuestoService.list();
        Presupuesto ultimo_presupuesto = presupuestos.get(presupuestos.size() - 1);
        return ResponseEntity.status(200).body(ultimo_presupuesto);
    }
}