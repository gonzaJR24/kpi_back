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

  public List<Empleado> listByArea(Area area) {
    return repository.findAllByArea(area);
  }

  public Empleado findById(int id) {
    return repository.findById(id).orElse(null);
  }

  public void deleteById(int id) {
    repository.deleteById(id);
  }

  public List<Empleado> findByArea(Area area) {
    return repository.findAllByArea(area);
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
    double totalWeight = numeroOperativosD * 1 + numeroOperativosC * 2 + numeroOperativosB * 3 + numeroOperativosA * 4;
    double base = ultimo_presupuesto.getMontoKpi() / totalWeight;
    List<Empleado> totalEmpleados = empleados;
    for (Empleado empleado : empleados) {
      switch (empleado.getCargo().getNombreCargo()) {
        case "Operativo A":
          empleado.setMonto(base * 4); // 4 veces la base
          break;
        case "Operativo B":
          empleado.setMonto(base * 3); // 3 veces la base
          break;
        case "Operativo C":
          empleado.setMonto(base * 2); // 2 veces la base
          break;
        case "Operativo D":
          empleado.setMonto(base); // Base
          break;
      }

      double porcentaje = (empleado.getMonto() * 100) / ultimo_presupuesto.getMontoKpi();
      empleado.setPorcentaje(porcentaje);
      repository.save(empleado);
    }

  }
}

