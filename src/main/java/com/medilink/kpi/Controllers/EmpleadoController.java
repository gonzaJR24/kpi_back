package com.medilink.kpi.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.medilink.kpi.Services.*;
import com.medilink.kpi.entities.Area;
import com.medilink.kpi.entities.Empleado;
import com.medilink.kpi.entities.Presupuesto;
import com.medilink.kpi.entities.Puntaje;
import com.medilink.kpi.entities.dto.AreaDTO;
import com.medilink.kpi.entities.dto.EmpleadoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/empleado")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private PresupuestoService presupuestoService;

    @Autowired
    private CargoService cargoService;

    @Autowired
    private AreaService areaService;

    @Autowired
    private PuntajeService puntajeService;

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody EmpleadoDTO empleadoDTO) {
        if (empleadoDTO.nombre().isEmpty() || empleadoDTO.apellido().isEmpty() || empleadoDTO.cargo() == 0) {
            return ResponseEntity.status(400).body("cannot be empty");
        }


        Empleado empleado = new Empleado();
        empleado.setNombre(empleadoDTO.nombre());
        empleado.setApellido(empleadoDTO.apellido());
        empleado.setCargo(cargoService.findById(empleadoDTO.cargo()));
        empleado.setArea(areaService.findById(empleadoDTO.area()));
        empleadoService.save(empleado);

        List<Presupuesto> presupuestos = presupuestoService.list();
        Presupuesto ultimo_presupuesto = presupuestos.get(presupuestos.size() - 1);
        actualizarPorcentaje(ultimo_presupuesto, empleadoService.list());

        return ResponseEntity.status(201).body(empleado);
    }


    @GetMapping
    public List<Empleado> list() throws JsonProcessingException {
        List<Puntaje>listaPuntaje=puntajeService.list();
        for(Puntaje puntaje:listaPuntaje){
            Empleado empleado=empleadoService.findById(puntaje.getEmpleado().getId());
            if(empleado!=null){
                empleado.setRendimiento((double) (puntaje.getPuntajeTotal() * 100) /70);
                empleadoService.save(empleado);
            }else{
                empleado.setRendimiento(0);
                empleadoService.save(empleado);
            }
        }
        return empleadoService.list();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empleado> findById(@PathVariable int id){
        Empleado empleado=empleadoService.findById(id);
        return ResponseEntity.status(200).body(empleado);
    }

    @GetMapping("/area/{area}")
    public List<Empleado> findByArea(@PathVariable int area) {
        Area area1 = areaService.findById(area);
        return empleadoService.findByArea(area1);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empleado> findById(@PathVariable int id, @RequestBody EmpleadoDTO empleadoDTO){
        Empleado empleado=empleadoService.findById(id);
        empleado.setNombre(empleadoDTO.nombre());
        empleado.setApellido(empleadoDTO.apellido());
        empleado.setArea(areaService.findById(empleadoDTO.area()));
        empleado.setCargo(cargoService.findById(empleadoDTO.cargo()));
        empleadoService.save(empleado);
        return ResponseEntity.status(200).body(empleado);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id){
        empleadoService.deleteById(id);
    }

    // Actualizar porcentaje
    public void actualizarPorcentaje(Presupuesto ultimo_presupuesto, List<Empleado> empleados) {
        // Inicializar contadores
        int numeroOperativosA = 0;
        int numeroOperativosB = 0;
        int numeroOperativosC = 0;
        int numeroOperativosD = 0;

        // Contar empleados
        for (Empleado empleado : empleados) {
            switch (empleado.getCargo().getNombreCargo()) {
                case "Operativo A":
                    numeroOperativosA++;
                    break;
                case "Operativo B":
                    numeroOperativosB++;
                    break;
                case "Operativo C":
                    numeroOperativosC++;
                    break;
                case "Operativo D":
                    numeroOperativosD++;
                    break;
            }
        }

        // Calcular el porcentaje
        double base = ultimo_presupuesto.getMontoKpi() / (numeroOperativosD + (numeroOperativosC * 2) + (numeroOperativosB * 3) + (numeroOperativosA * 4));
        List<Empleado> totalEmpleados = empleadoService.list();
        for (Empleado empleado : totalEmpleados) {
            switch (empleado.getCargo().getNombreCargo()) {
                case "Operativo A":
                    double base_porcentual1 = (base * 100) / ultimo_presupuesto.getMontoKpi();
                    double porcentaje1 = base_porcentual1 * numeroOperativosA * 4;
                    empleado.setPorcentaje(porcentaje1/numeroOperativosA);
                    empleado.setMonto((ultimo_presupuesto.getMontoKpi() * (porcentaje1 / 100)) / numeroOperativosA);
                    empleadoService.save(empleado);
                    break;
                case "Operativo B":
                    double base_porcentual2 = (base * 100) / ultimo_presupuesto.getMontoKpi();
                    double porcentaje2 = base_porcentual2 * numeroOperativosB * 3;
                    empleado.setPorcentaje(porcentaje2/numeroOperativosB);
                    empleado.setMonto((ultimo_presupuesto.getMontoKpi() * (porcentaje2 / 100)) / numeroOperativosB);
                    empleadoService.save(empleado);
                    break;
                case "Operativo C":
                    double base_porcentual3 = (base * 100) / ultimo_presupuesto.getMontoKpi();
                    double porcentaje3 = base_porcentual3 * numeroOperativosC * 2;
                    empleado.setPorcentaje(porcentaje3/numeroOperativosC);
                    empleado.setMonto((ultimo_presupuesto.getMontoKpi() * (porcentaje3 / 100)) / numeroOperativosC);
                    empleadoService.save(empleado);
                    break;
                case "Operativo D":
                    double base_porcentual4 = (base * 100) / ultimo_presupuesto.getMontoKpi();
                    double porcentaje4 = base_porcentual4 * numeroOperativosD;
                    empleado.setPorcentaje(porcentaje4/numeroOperativosD);
                    empleado.setMonto((ultimo_presupuesto.getMontoKpi() * (porcentaje4 / 100)) / numeroOperativosD);
                    empleadoService.save(empleado);
                    break;
            }
//            empleado.getCargo().getPresupuesto().setMontoKpi(ultimo_presupuesto.getMontoKpi());
//            List<Puntaje>listaPuntaje=puntajeService.list();
//            for(Puntaje puntaje:listaPuntaje){
//                if(puntaje.getEmpleado().getId().equals(empleado.getId())){
//                    empleado.setRendimiento((double) (puntaje.getPuntajeTotal() * 100) /70);
//                }else{
//                    empleado.setRendimiento(0);
//                }
//            }
        }
    }
}

