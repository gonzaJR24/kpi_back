package com.medilink.kpi.Controllers;

import com.medilink.kpi.Services.EmpleadoService;
import com.medilink.kpi.Services.PresupuestoService;
import com.medilink.kpi.Services.PuntajeService;
import com.medilink.kpi.entities.Empleado;
import com.medilink.kpi.entities.Presupuesto;
import com.medilink.kpi.entities.Puntaje;
import com.medilink.kpi.entities.dto.EditPuntajeDTO;
import com.medilink.kpi.entities.dto.PuntajeDTO;
import com.medilink.kpi.entities.dto.PuntajeRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/puntaje")
@CrossOrigin
public class PuntajeController {

    @Autowired
    private PuntajeService puntajeService;

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private PresupuestoService presupuestoService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody PuntajeDTO puntajeDTO){
        if(puntajeDTO==null){
            return ResponseEntity.status(400).body("Invalid or empty value");
        }
        Puntaje puntaje=new Puntaje();
        puntaje.setAusenciaPuntualidad(puntajeDTO.ausenciaPuntualidad());
        puntaje.setEspecifico1(puntajeDTO.especifico1());
        puntaje.setEspecifico2(puntajeDTO.especifico2());
        puntaje.setNps(puntajeDTO.nps());
        puntaje.setActitudesGestionComportamiento(puntajeDTO.actitudesGestionComportamiento());
        puntaje.setCalificacionLider(puntajeDTO.calificacionLider());

        int sumaPuntajes=puntajeDTO.ausenciaPuntualidad()+puntajeDTO.especifico1()+puntajeDTO.especifico2()+puntajeDTO.nps()+
                puntajeDTO.actitudesGestionComportamiento()+puntajeDTO.calificacionLider();

        puntaje.setPuntajeTotal(sumaPuntajes);
        puntaje.setComentario(puntajeDTO.comentario());
        puntaje.setEmpleado(empleadoService.findById(puntajeDTO.empleado()));
        puntaje.setFechaEvaluacion(LocalDate.now());

        if(puntajeDTO.especifico1()<2 || puntajeDTO.especifico2()<2){
            puntaje.setAusenciaPuntualidad(0);
            puntaje.setEspecifico1(0);
            puntaje.setEspecifico2(0);
            puntaje.setNps(0);
            puntaje.setActitudesGestionComportamiento(0);
            puntaje.setCalificacionLider(0);
            puntaje.setPuntajeTotal(sumaPuntajes);
        }
        puntaje.setMes(puntajeDTO.mes());
        puntaje.setAnio((puntajeDTO.anio()));

        puntajeService.save(puntaje);
        return ResponseEntity.status(201).body(puntajeDTO);
    }

    @GetMapping
    public List<Puntaje> list(){
        return puntajeService.list();
    }

  @PutMapping("{id}")
  public ResponseEntity<?> edit(@PathVariable int id, @RequestBody EditPuntajeDTO puntajeDTO) {
    Puntaje puntaje = puntajeService.findById(id);
    puntaje.setActitudesGestionComportamiento(puntajeDTO.actitudesGestionComportamiento());
    puntaje.setAusenciaPuntualidad(puntajeDTO.ausenciaPuntualidad());
    puntaje.setCalificacionLider(puntajeDTO.calificacionLider());
    puntaje.setNps(puntajeDTO.nps());
    puntaje.setEspecifico1(puntajeDTO.especifico1());
    puntaje.setEspecifico2(puntajeDTO.especifico2());
    puntaje.setComentario(puntajeDTO.comentario());

    int sumaPuntajes = puntajeDTO.ausenciaPuntualidad() + puntajeDTO.especifico1() + puntajeDTO.especifico2() + puntajeDTO.nps() +
      puntajeDTO.actitudesGestionComportamiento() + puntajeDTO.calificacionLider();
    puntaje.setPuntajeTotal(sumaPuntajes);

    puntajeService.save(puntaje);

    // Actualizar el rendimiento del empleado
    Empleado empleado = puntaje.getEmpleado();
    if (empleado != null) {
      empleado.setRendimiento((double) (sumaPuntajes * 100) / 60); // Ajusta la fórmula según tu lógica
      empleadoService.save(empleado);
    }

    // Actualizar los montos de todos los empleados
    updateDatosEmpleado();

    return ResponseEntity.status(200).body(puntaje);
  }

  private void updateDatosEmpleado() {
    List<Presupuesto> presupuestos = presupuestoService.list();
    if (!presupuestos.isEmpty()) {
      Presupuesto ultimo_presupuesto = presupuestos.get(presupuestos.size() - 1);
      actualizarPorcentaje(ultimo_presupuesto, empleadoService.list());
    }
  }

  private void actualizarPorcentaje(Presupuesto ultimo_presupuesto, List<Empleado> empleados) {
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable int id){
      puntajeService.deleteById(id);
      Map<String, String> response=new HashMap<>();
      response.put("response","deleted");
      return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/find")
    public List<Puntaje> puntajes(@RequestBody PuntajeRequestDTO puntajeRequestDTO){
      return puntajeService.findAllPuntaje(puntajeRequestDTO.mes(), puntajeRequestDTO.anio());
    }
}
