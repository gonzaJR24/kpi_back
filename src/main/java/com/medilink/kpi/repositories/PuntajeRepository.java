package com.medilink.kpi.repositories;

import com.medilink.kpi.entities.Empleado;
import com.medilink.kpi.entities.Puntaje;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PuntajeRepository extends JpaRepository<Puntaje, Integer> {
  public List<Puntaje> findAllByMesAndAnio(int mes, int anio);
  public List<Puntaje> findByEmpleado(Empleado empleado);
}
