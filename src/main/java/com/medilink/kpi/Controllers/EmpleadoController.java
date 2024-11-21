package com.medilink.kpi.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medilink.kpi.Services.*;
import com.medilink.kpi.entities.Empleado;
import com.medilink.kpi.entities.Presupuesto;
import com.medilink.kpi.entities.dto.EmpleadoDTO;
import com.medilink.kpi.entities.dto.EmpleadoEditDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empleado")
@CrossOrigin
public class EmpleadoController {
    int numeroOperativosA = 0;
    int numeroOperativosB = 0;
    int numeroOperativosC = 0;
    int numeroOperativosD = 0;

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private PresupuestoService presupuestoService;

    @Autowired
    private CargoService cargoService;

    @Autowired
    private SexoService sexoService;

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
        empleado.setSexo(sexoService.findById(empleadoDTO.sexo()));
        empleado.setArea(areaService.findById(empleadoDTO.area()));
        empleadoService.save(empleado);

        List<Presupuesto> presupuestos = presupuestoService.list();
        if (!presupuestos.isEmpty()) {
            Presupuesto ultimo_presupuesto = presupuestos.get(presupuestos.size() - 1);
            actualizarPorcentaje(ultimo_presupuesto);
        }

        return ResponseEntity.status(201).body(empleadoDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable int id) {
        Empleado empleado = empleadoService.findById(id);
        return ResponseEntity.status(200).body(empleado);
    }

    @GetMapping
    public List<Empleado> list() throws JsonProcessingException {
        return empleadoService.list();
    }

    // Actualizar porcentaje
    public void actualizarPorcentaje(Presupuesto ultimo_presupuesto) {
        if (ultimo_presupuesto == null || ultimo_presupuesto.getMontoKpi() == 0) {
            System.out.println("El último presupuesto es null o no tiene montoKpi.");
            return; // No procede con los cálculos si el presupuesto no es válido
        }

        // Contar empleados
        for (Empleado empleado : empleadoService.list()) {
            if (empleado.getCargo() != null) {
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
        }

        // Calcular el porcentaje solo si hay empleados de tipo "Operativo D"
        if (numeroOperativosD > 0) {
            List<Empleado> totalEmpleados = empleadoService.list();
            for (Empleado empleado : totalEmpleados) {
                if (empleado.getCargo() != null) {
                    switch (empleado.getCargo().getNombreCargo()) {
                        case "Operativo A":
                            calcularEstablecerPorcentaje(empleado, ultimo_presupuesto, 4, numeroOperativosA);
                            break;
                        case "Operativo B":
                            calcularEstablecerPorcentaje(empleado, ultimo_presupuesto, 3, numeroOperativosB);
                            break;
                        case "Operativo C":
                            calcularEstablecerPorcentaje(empleado, ultimo_presupuesto, 2, numeroOperativosC);
                            break;
                        case "Operativo D":
                            calcularEstablecerPorcentaje(empleado, ultimo_presupuesto, 1, numeroOperativosD);
                            break;
                    }
                }
            }
        } else {
            System.out.println("No hay empleados de tipo Operativo D.");
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll() {
        empleadoService.deleteAll();
        return ResponseEntity.status(200).body("deleted successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable int id) {
        if (id != 0) {
            empleadoService.deleteById(id);
            return ResponseEntity.status(200).body("deleted successfully");
        }
        return ResponseEntity.status(400).body("user not found");
    }

    // Calcular y establecer el porcentaje y monto
    public void calcularEstablecerPorcentaje(Empleado empleado, Presupuesto ultimo_presupuesto, int multiplicador, int numeroOperativos) {
        // Validar que el último presupuesto tenga un montoKpi no nulo
        if (ultimo_presupuesto == null || ultimo_presupuesto.getMontoKpi() == 0) {
            System.out.println("El montoKpi es null para el presupuesto.");
            return; // No continúa si no hay montoKpi
        }

        double base = ultimo_presupuesto.getMontoKpi() / (numeroOperativosD + (numeroOperativosC * 2) + (numeroOperativosB * 3) + (numeroOperativosA * 4));
        double base_porcentual = (base * 100) / ultimo_presupuesto.getMontoKpi();
        double porcentaje = base_porcentual * numeroOperativos * multiplicador;
        empleado.setPorcentaje(porcentaje);
        empleado.setMonto((ultimo_presupuesto.getMontoKpi() * (porcentaje / 100)) / numeroOperativos);
        empleadoService.save(empleado);
    }

    @PutMapping
    public ResponseEntity<?> edit(@RequestBody EmpleadoEditDTO empleadoDTO) {
        if (empleadoDTO.nombre().isEmpty() || empleadoDTO.apellido().isEmpty() || empleadoDTO.cargo() == 0) {
            return ResponseEntity.status(400).body("cannot be empty");
        }

        Empleado empleado = empleadoService.findById(empleadoDTO.id());
        empleado.setNombre(empleadoDTO.nombre());
        empleado.setApellido(empleadoDTO.apellido());
        empleado.setSexo(sexoService.findById(empleadoDTO.sexo()));
        empleado.setCargo(cargoService.findById(empleadoDTO.cargo()));
        empleado.setArea(areaService.findById(empleadoDTO.area()));
        empleadoService.save(empleado);

        List<Presupuesto> presupuestos = presupuestoService.list();
        if (!presupuestos.isEmpty()) {
            Presupuesto ultimo_presupuesto = presupuestos.get(presupuestos.size() - 1);
            actualizarPorcentaje(ultimo_presupuesto);
        }

        return ResponseEntity.status(201).body(empleadoDTO);
    }
}
