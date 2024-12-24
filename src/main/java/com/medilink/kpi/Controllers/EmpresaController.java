package com.medilink.kpi.Controllers;

import com.medilink.kpi.Services.EmpleadoService;
import com.medilink.kpi.Services.EmpresaService;
import com.medilink.kpi.Services.PresupuestoService;
import com.medilink.kpi.entities.Empresa;
import com.medilink.kpi.entities.Presupuesto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

@RestController
@RequestMapping("/api/empresa")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private PresupuestoService presupuestoService;

    @Autowired
    private EmpleadoService empleadoService;



    @PostMapping
    public ResponseEntity<?> save(@RequestBody Empresa empresa) {
        if (empresa.getNombreEmpresa().isEmpty() || empresa.getProgresoEmpresa() == 0 || empresa.getValorMeta() == 0) {
            return ResponseEntity.status(400).body("invalid or empty values");
        }

        empresaService.save(empresa);
        cargarPresupuesto(empresa);
        return ResponseEntity.status(201).body("created successfully");
    }

    @GetMapping
    public List<Empresa>list(){
        return empresaService.list();
    }

    @DeleteMapping
    public void delete(){
        empresaService.delete();
    }

    @PutMapping
    public ResponseEntity<Object> edit(@RequestBody Empresa empresa){
        if(empresa==null){
            return ResponseEntity.status(400).body("cannot be null");
        }

        if(empresa.getProgresoEmpresa()==0 || empresa.getValorMeta()==0){
            return ResponseEntity.status(404).body("Invalid or empty values");
        }

        empresaService.edit(empresa);
        cargarPresupuesto(empresa);

        List<Presupuesto> presupuestos = presupuestoService.list();
        Presupuesto ultimo_presupuesto = presupuestos.get(presupuestos.size() - 1);
        empleadoService.actualizarPorcentaje(ultimo_presupuesto, empleadoService.list());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(empresa);
    }


    //Cargar presupuesto
    public void cargarPresupuesto(Empresa empresa) {
        double porcientoKpi2 = (empresa.getProgresoEmpresa() * 100.0) / empresa.getValorMeta();
        DecimalFormat decimalFormat=new DecimalFormat("#.#");
        double porcientoKpi= Double.parseDouble(decimalFormat.format(porcientoKpi2));

        // Crear Presupuesto
        Presupuesto presupuesto = new Presupuesto();

        if (porcientoKpi < 70) {
            presupuesto.setMontoKpi(0);
            presupuestoService.save(presupuesto);
        } else if (porcientoKpi >= 70 && porcientoKpi < 89.4) {//funciona
            presupuesto.setMontoKpi(empresa.getProgresoEmpresa() * 0.0125);
            presupuestoService.save(presupuesto);
        } else if (porcientoKpi > 89.5 && porcientoKpi < 99.5) {
            presupuesto.setMontoKpi(empresa.getProgresoEmpresa() * 0.0150);
            presupuestoService.save(presupuesto);
        } else if (porcientoKpi >= 99.5  && porcientoKpi<100.5) {
            presupuesto.setMontoKpi(empresa.getProgresoEmpresa() * 0.0175);
            presupuestoService.save(presupuesto);
        } else if (porcientoKpi >= 100.5) {
            presupuesto.setMontoKpi(empresa.getProgresoEmpresa() * 0.03);
            presupuestoService.save(presupuesto);
        }
    }

}