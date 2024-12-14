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
        empleadoService.actualizarPorcentaje(ultimo_presupuesto);

        return ResponseEntity.status(201).body(empleado);
    }


    @GetMapping
    public List<Empleado> list() throws JsonProcessingException {
      List<Presupuesto> presupuestos = presupuestoService.list();
      Presupuesto ultimo_presupuesto = presupuestos.get(presupuestos.size()-1);
      empleadoService.actualizarPorcentaje(ultimo_presupuesto);
        return empleadoService.list();
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id){
      empleadoService.deleteById(id);
      return "deleted successfully";
    }
}

