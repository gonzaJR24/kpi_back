package com.medilink.kpi.repositories;

import com.medilink.kpi.entities.Area;
import com.medilink.kpi.entities.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {
 public List<Empleado> findByArea(Area area);
}
