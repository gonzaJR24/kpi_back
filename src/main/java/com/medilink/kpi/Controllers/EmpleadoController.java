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
import java.util.Map;

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
    updateDatosEmpleado();
    return ResponseEntity.status(201).body(empleado);
  }

  @GetMapping
  public List<Empleado> list() throws JsonProcessingException {
    updateDatosEmpleado();
    List<Puntaje> listaPuntaje = puntajeService.list();
    for (Puntaje puntaje : listaPuntaje) {
      Empleado empleado = empleadoService.findById(puntaje.getEmpleado().getId());
      if (empleado != null) {
        empleado.setRendimiento((double) (puntaje.getPuntajeTotal() * 100) / 60);
        empleadoService.save(empleado);
      }
    }
    return empleadoService.list();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Empleado> findById(@PathVariable int id) {
    updateDatosEmpleado();
    Empleado empleado = empleadoService.findById(id);
    return ResponseEntity.status(200).body(empleado);
  }

  @PostMapping("/findByArea")
  public List<Empleado> findByArea(@RequestBody Map<String, String> body) {
    String areaStr = body.get("area");
    Area area = areaService.findByNombre(areaStr);
    updateDatosEmpleado();
    return empleadoService.findByArea(area);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Empleado> update(@PathVariable int id, @RequestBody EmpleadoDTO empleadoDTO) {
    Empleado empleado = empleadoService.findById(id);
    empleado.setNombre(empleadoDTO.nombre());
    empleado.setApellido(empleadoDTO.apellido());
    empleado.setArea(areaService.findById(empleadoDTO.area()));
    empleado.setCargo(cargoService.findById(empleadoDTO.cargo()));
    empleadoService.save(empleado);
    updateDatosEmpleado();
    return ResponseEntity.status(200).body(empleado);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable int id) {
    empleadoService.deleteById(id);
    updateDatosEmpleado();
  }

  public void updateDatosEmpleado() {
    List<Presupuesto> presupuestos = presupuestoService.list();
    if (!presupuestos.isEmpty()) {
      Presupuesto ultimo_presupuesto = presupuestos.get(presupuestos.size() - 1);
      actualizarPorcentaje(ultimo_presupuesto, empleadoService.list());
    }
  }

  public void actualizarPorcentaje(Presupuesto ultimo_presupuesto, List<Empleado> empleados) {
    int numeroOperativosA = 0;
    int numeroOperativosB = 0;
    int numeroOperativosC = 0;
    int numeroOperativosD = 0;

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

    double base = ultimo_presupuesto.getMontoKpi() / (numeroOperativosD + (numeroOperativosC * 2) + (numeroOperativosB * 3) + (numeroOperativosA * 4));
    for (Empleado empleado : empleados) {
      switch (empleado.getCargo().getNombreCargo()) {
        case "Operativo A":
          double porcentaje1 = (base * 100 * 4) / ultimo_presupuesto.getMontoKpi();
          empleado.setPorcentaje(porcentaje1 / numeroOperativosA);
          empleado.setMonto((ultimo_presupuesto.getMontoKpi() * (porcentaje1 / 100)) / numeroOperativosA);
          break;
        case "Operativo B":
          double porcentaje2 = (base * 100 * 3) / ultimo_presupuesto.getMontoKpi();
          empleado.setPorcentaje(porcentaje2 / numeroOperativosB);
          empleado.setMonto((ultimo_presupuesto.getMontoKpi() * (porcentaje2 / 100)) / numeroOperativosB);
          break;
        case "Operativo C":
          double porcentaje3 = (base * 100 * 2) / ultimo_presupuesto.getMontoKpi();
          empleado.setPorcentaje(porcentaje3 / numeroOperativosC);
          empleado.setMonto((ultimo_presupuesto.getMontoKpi() * (porcentaje3 / 100)) / numeroOperativosC);
          break;
        case "Operativo D":
          double porcentaje4 = (base * 100) / ultimo_presupuesto.getMontoKpi();
          empleado.setPorcentaje(porcentaje4 / numeroOperativosD);
          empleado.setMonto((ultimo_presupuesto.getMontoKpi() * (porcentaje4 / 100)) / numeroOperativosD);
          break;
      }
      empleadoService.save(empleado);
    }
  }

  @GetMapping("/update-and-get/{id}")
  public ResponseEntity<Empleado> updateAndGetEmpleado(@PathVariable int id) {
    updateDatosEmpleado(); // Actualiza los datos de todos los empleados
    Empleado empleado = empleadoService.findById(id);
    return ResponseEntity.status(200).body(empleado);
  }
}
