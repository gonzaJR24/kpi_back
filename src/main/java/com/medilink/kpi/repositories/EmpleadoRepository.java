package com.medilink.kpi.repositories;

import com.medilink.kpi.entities.Area;
import com.medilink.kpi.entities.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {
 public List<Empleado> findByArea(Area area);

}
