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

import java.text.DecimalFormat;
import java.util.*;

@RestController
@RequestMapping("/api/area")
@CrossOrigin
public class AreaController {

    private static final int NUMERO_DE_CRITERIOS = 6;
    private final List<String> NOMBRE_AREAS = Arrays.asList("Productividad", "Servicios Generales", "Direccion Medica", "Facturacion", "Operaciones", "Contabilidad");

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
            return ResponseEntity.status(400).body("Invalid or empty values");
        }

        Area area = new Area();
        area.setNombreArea(areaDTO.nombreArea());
        area.setSucursal(sucursalService.findById(areaDTO.sucursal()));

        actualizarRendimiento();
        areaService.save(area);
        return ResponseEntity.status(201).body(areaDTO);
    }

    @GetMapping
    public List<Area> list() {
        actualizarRendimiento();
        return areaService.list();
    }

    private void actualizarRendimiento() {
        List<Empleado> listaEmpleados = empleadoService.list();
        List<Area> areas = areaService.list();
        Map<String, Integer> cantidad_empleados_areas = new HashMap<>();

        for (String areaName : NOMBRE_AREAS) {
            cantidad_empleados_areas.put(areaName, 0);
        }

        for (Area area : areas) {
            int puntajeArea = 0;
            int numero_empleados = 0;

            for (Empleado empleado : listaEmpleados) {
                if (empleado.getArea() != null && empleado.getArea().getNombreArea().equals(area.getNombreArea())) {
                    numero_empleados++;
                    for (Puntaje puntaje : empleado.getPuntajes()) {
                        puntajeArea += puntaje.getPuntajeTotal();
                    }
                }
            }

            if (numero_empleados > 0) {
                area.setPuntajeTotal(cantidad_empleados_areas.get(area.getNombreArea()));
                area.setPuntajeTotal(puntajeArea);
                calcularEstablecerRendimiento(puntajeArea, numero_empleados, area);
            } else {
                area.setPuntajeTotal(0);
                area.setCantidadEmpleados(0);
            }
        }
    }

    private void calcularEstablecerRendimiento(double puntajeContabilidad, int cantidadEmpleados, Area area) {
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        double rendimiento = (puntajeContabilidad / cantidadEmpleados) / NUMERO_DE_CRITERIOS;
        double rendimientoFormateado = Double.parseDouble(decimalFormat.format(rendimiento));
        area.setRendimientoArea(rendimientoFormateado);
        area.setCantidadEmpleados(cantidadEmpleados);
        areaService.save(area);
    }
}