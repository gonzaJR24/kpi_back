package com.medilink.kpi.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medilink.kpi.Services.*;
import com.medilink.kpi.entities.Empleado;
import com.medilink.kpi.entities.Presupuesto;
import com.medilink.kpi.entities.dto.EmpleadoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
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
    private ObjectMapper objectMapper;

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

        // Obtener la lista actualizada de empleados y recalcular
        List<Presupuesto> presupuestos = presupuestoService.list();
        Presupuesto ultimo_presupuesto = presupuestos.get(presupuestos.size() - 1);

        // Volver a calcular porcentajes y montos después de asegurarse de que el nuevo empleado está en la lista
        actualizarPorcentaje(ultimo_presupuesto);

        return ResponseEntity.status(201).body(empleado);
    }


    @GetMapping
    public List<Empleado> list() throws JsonProcessingException {
        return empleadoService.list();
    }

    // Actualizar porcentaje
    public void actualizarPorcentaje(Presupuesto ultimo_presupuesto) {
        // Inicializar contadores
        int numeroOperativosA = 0;
        int numeroOperativosB = 0;
        int numeroOperativosC = 0;
        int numeroOperativosD = 0;

        // Contar empleados
        for (Empleado empleado : empleadoService.list()) {
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
                    empleado.setPorcentaje(porcentaje1);
                    empleado.setMonto((ultimo_presupuesto.getMontoKpi() * (porcentaje1 / 100)) / numeroOperativosA);
                    empleadoService.save(empleado);
                    break;
                case "Operativo B":
                    double base_porcentual2 = (base * 100) / ultimo_presupuesto.getMontoKpi();
                    double porcentaje2 = base_porcentual2 * numeroOperativosB * 3;
                    empleado.setPorcentaje(porcentaje2);
                    empleado.setMonto((ultimo_presupuesto.getMontoKpi() * (porcentaje2 / 100)) / numeroOperativosB);
                    empleadoService.save(empleado);
                    break;
                case "Operativo C":
                    double base_porcentual3 = (base * 100) / ultimo_presupuesto.getMontoKpi();
                    double porcentaje3 = base_porcentual3 * numeroOperativosC * 2;
                    empleado.setPorcentaje(porcentaje3);
                    empleado.setMonto((ultimo_presupuesto.getMontoKpi() * (porcentaje3 / 100)) / numeroOperativosC);
                    empleadoService.save(empleado);
                    break;
                case "Operativo D":
                    double base_porcentual4 = (base * 100) / ultimo_presupuesto.getMontoKpi();
                    double porcentaje4 = base_porcentual4 * numeroOperativosD;
                    empleado.setPorcentaje(porcentaje4);
                    empleado.setMonto((ultimo_presupuesto.getMontoKpi() * (porcentaje4 / 100)) / numeroOperativosD);
                    empleadoService.save(empleado);
                    break;
            }
            empleado.getCargo().getPresupuesto().setMontoKpi(ultimo_presupuesto.getMontoKpi());
            empleadoService.save(empleado);
        }
    }
}

