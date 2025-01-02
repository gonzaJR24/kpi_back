package com.medilink.kpi.Services;

import com.medilink.kpi.entities.Area;
import com.medilink.kpi.entities.Empleado;
import com.medilink.kpi.entities.Presupuesto;
import com.medilink.kpi.repositories.EmpleadoRepository;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmpleadoService {


    @Autowired
    private EmpleadoRepository repository;


    public void save(Empleado empleado) {
        repository.save(empleado);
    }

    public List<Empleado> list() {
        return repository.findAll();
    }

    public Empleado findById(int id){
        return repository.findById(id).orElse(null);
    }

    public void deleteById(int id){
        repository.deleteById(id);
    }

    public List<Empleado> findByArea(Area area){
        return repository.findByArea(area);
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
        List<Empleado> totalEmpleados = empleados;
        for (Empleado empleado : totalEmpleados) {
            switch (empleado.getCargo().getNombreCargo()) {
                case "Operativo A":
                    double base_porcentual1 = (base * 100) / ultimo_presupuesto.getMontoKpi();
                    double porcentaje1 = base_porcentual1 * numeroOperativosA * 4;
                    empleado.setPorcentaje(porcentaje1);
                    empleado.setMonto((ultimo_presupuesto.getMontoKpi() * (porcentaje1 / 100)) / numeroOperativosA);
                    repository.save(empleado);
                    break;
                case "Operativo B":
                    double base_porcentual2 = (base * 100) / ultimo_presupuesto.getMontoKpi();
                    double porcentaje2 = base_porcentual2 * numeroOperativosB * 3;
                    empleado.setPorcentaje(porcentaje2);
                    empleado.setMonto((ultimo_presupuesto.getMontoKpi() * (porcentaje2 / 100)) / numeroOperativosB);
                    empleado.getCargo().getPresupuesto().setMontoKpi(ultimo_presupuesto.getMontoKpi());
                    repository.save(empleado);
                    break;
                case "Operativo C":
                    double base_porcentual3 = (base * 100) / ultimo_presupuesto.getMontoKpi();
                    double porcentaje3 = base_porcentual3 * numeroOperativosC * 2;
                    empleado.setPorcentaje(porcentaje3);
                    empleado.setMonto((ultimo_presupuesto.getMontoKpi() * (porcentaje3 / 100)) / numeroOperativosC);
                    empleado.getCargo().getPresupuesto().setMontoKpi(ultimo_presupuesto.getMontoKpi());
                    repository.save(empleado);
                    break;
                case "Operativo D":
                    double base_porcentual4 = (base * 100) / ultimo_presupuesto.getMontoKpi();
                    double porcentaje4 = base_porcentual4 * numeroOperativosD;
                    empleado.setPorcentaje(porcentaje4);
                    empleado.setMonto((ultimo_presupuesto.getMontoKpi() * (porcentaje4 / 100)) / numeroOperativosD);
                    empleado.getCargo().getPresupuesto().setMontoKpi(ultimo_presupuesto.getMontoKpi());
                    repository.save(empleado);
                    break;
            }

        }
    }

}

