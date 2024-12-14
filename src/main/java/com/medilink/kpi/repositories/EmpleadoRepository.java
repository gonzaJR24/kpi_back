package com.medilink.kpi.repositories;

import com.medilink.kpi.entities.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {
}
