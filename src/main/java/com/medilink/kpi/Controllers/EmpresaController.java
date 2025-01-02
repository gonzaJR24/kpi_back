
package com.medilink.kpi.Controllers;

import com.medilink.kpi.Services.EmpresaService;
import com.medilink.kpi.Services.PresupuestoService;
import com.medilink.kpi.entities.Empresa;
import com.medilink.kpi.entities.Presupuesto;
import com.medilink.kpi.entities.dto.EmpresaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.List;

@RestController
@RequestMapping("/api/empresa")
@CrossOrigin
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private PresupuestoService presupuestoService;

//    Por el momento no es necesario agregar empresa

//    @PostMapping
//    public ResponseEntity<?> save(@RequestBody Empresa empresa) {
//        if (empresa.getNombreEmpresa().isEmpty() || empresa.getProgresoEmpresa() == 0 || empresa.getValorMeta() == 0) {
//            return ResponseEntity.status(400).body("invalid or empty values");
//        }
//
//        empresaService.save(empresa);
//        cargarPresupuesto(empresa);
//        return ResponseEntity.status(201).body("created successfully");
//    }

    @GetMapping
    public List<Empresa>list(){
        return empresaService.list();
    }

    @PutMapping
    public ResponseEntity<Object> edit(@RequestBody EmpresaDTO empresaDTO){
        Empresa empresa=empresaService.findById(1); //Solo tengo una empresa, pondre el primer id
        if(empresaDTO==null){
            return ResponseEntity.status(400).body("cannot be null");
        }


        empresa.setProgresoEmpresa(empresaDTO.progresoEmpresa());
        empresa.setValorMeta(empresaDTO.valorMeta());
        empresaService.save(empresa);
        cargarPresupuesto(empresa);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(empresa);
    }


    public void cargarPresupuesto(Empresa empresa) {
        double porcientoKpi2 = (empresa.getProgresoEmpresa() * 100.0) / empresa.getValorMeta();
        DecimalFormat decimalFormat=new DecimalFormat("#.#");
        double porcientoKpi= Double.parseDouble(decimalFormat.format(porcientoKpi2));

        // Crear Presupuesto
        Presupuesto presupuesto = new Presupuesto();

        if (porcientoKpi <= 70) {
            presupuesto.setMontoKpi(0);
            presupuestoService.save(presupuesto);
        } else if (porcientoKpi >= 70.5 && porcientoKpi < 89.4) {
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
