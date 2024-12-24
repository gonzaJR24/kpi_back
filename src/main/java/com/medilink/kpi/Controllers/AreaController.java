package com.medilink.kpi.Controllers;

import com.medilink.kpi.Services.AreaService;
import com.medilink.kpi.Services.EmpleadoService;
import com.medilink.kpi.Services.PuntajeService;
import com.medilink.kpi.Services.SucursalService;
import com.medilink.kpi.entities.Area;
import com.medilink.kpi.entities.Empleado;
import com.medilink.kpi.entities.Puntaje;
import com.medilink.kpi.entities.dto.AreaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/area")
@CrossOrigin
public class AreaController {

    @Autowired
    private AreaService areaService;

    @Autowired
    private SucursalService sucursalService;

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private PuntajeService puntajeService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody AreaDTO areaDTO) {
        if (areaDTO.nombreArea().isEmpty() || areaDTO.sucursal() == 0) {
            return ResponseEntity.status(400).body("invalid or empty values");
        }

        Area area = new Area();
        area.setNombreArea(areaDTO.nombreArea());
        area.setSucursal(sucursalService.findById(areaDTO.sucursal()));
        area.setGerente(areaDTO.gerente());

        totalEmpleados();
        areaService.save(area);
        return ResponseEntity.status(201).body(area);
    }

    @GetMapping
    public List<Area> list() {
        totalEmpleados();
        return areaService.list();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> edit(@PathVariable int id, @RequestBody AreaDTO areaDTO){
        Area area=areaService.findById(id);
        if(area==null){
            return ResponseEntity.status(404).body("Area not found");
        }
        area.setNombreArea(areaDTO.nombreArea());
        area.setSucursal(sucursalService.findById(areaDTO.sucursal()));
        area.setGerente(areaDTO.gerente());
        areaService.save(area);
        return ResponseEntity.status(201).body(areaDTO);
    }

    public void totalEmpleados() {
        int cantidadEmpleados = 0;
        List<Empleado> listaEmpleados = empleadoService.list();

        for (Empleado empleado : listaEmpleados) {
            List<Area> areas = areaService.list();
            for (Area area : areas) {
                if (empleado.getArea() != null) {
                    if (empleado.getArea().getNombreArea().equals(area.getNombreArea())) {
                        area.setCantidadEmpleados(++cantidadEmpleados);
                    }
                }
            }
        }
    }

//    public void rendimientoEmpleado() {
//        double puntajes = 0;
//        int cantidadEmpleados = 0;
//        List<Empleado> listaEmpleados = empleadoService.list();
//
//        for (Empleado empleado : listaEmpleados) {
//            List<Area> areas = areaService.list();
//            for (Area area : areas) {
//                area.setRendimientoArea(300);
//                if (empleado.getArea().getNombreArea().equals(area.getNombreArea())) {
//                    List<Puntaje> listaPuntajes = empleado.getPuntajes();
//                    for (Puntaje puntaje : listaPuntajes) {
//                        puntajes += puntaje.getPuntajeTotal();
//                    }
//                    if (puntajes != 0) {
//                        area.setRendimientoArea(puntajes);
//                    }
//                }
//            }
//        }
//
//    }
}