package com.medilink.kpi.Controllers;

import com.medilink.kpi.Services.EmpleadoService;
import com.medilink.kpi.Services.PuntajeService;
import com.medilink.kpi.entities.Puntaje;
import com.medilink.kpi.entities.dto.EditPuntajeDTO;
import com.medilink.kpi.entities.dto.PuntajeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/puntaje")
@CrossOrigin
public class PuntajeController {

    @Autowired
    private PuntajeService puntajeService;

    @Autowired
    private EmpleadoService empleadoService;

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

        puntajeService.save(puntaje);
        return ResponseEntity.status(201).body(puntajeDTO);
    }

    @GetMapping
    public List<Puntaje> list(){
        return puntajeService.list();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> edit(@PathVariable int id, @RequestBody EditPuntajeDTO puntajeDTO){
        Puntaje puntaje=puntajeService.findById(id);
        puntaje.setActitudesGestionComportamiento(puntajeDTO.actitudesGestionComportamiento());
        puntaje.setAusenciaPuntualidad(puntajeDTO.ausenciaPuntualidad());
        puntaje.setCalificacionLider(puntajeDTO.calificacionLider());
        puntaje.setNps(puntajeDTO.nps());
        puntaje.setEspecifico1(puntajeDTO.especifico1());
        puntaje.setEspecifico2(puntajeDTO.especifico2());
        puntaje.setComentario(puntajeDTO.comentario());
        int sumaPuntajes=puntajeDTO.ausenciaPuntualidad()+puntajeDTO.especifico1()+puntajeDTO.especifico2()+puntajeDTO.nps()+
                puntajeDTO.actitudesGestionComportamiento()+puntajeDTO.calificacionLider();
        puntaje.setPuntajeTotal(sumaPuntajes);
        puntajeService.save(puntaje);
        return ResponseEntity.status(200).body(puntaje);
    }
}
