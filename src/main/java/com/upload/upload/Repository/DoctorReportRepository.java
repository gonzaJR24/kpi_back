package com.upload.upload.Repository;

import com.upload.upload.entities.DoctorReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface DoctorReportRepository extends JpaRepository<DoctorReportEntity, Integer> {
  public List<DoctorReportEntity> findByMesAndAnioAndProveedor(int month, int year, String proveedor);
  public DoctorReportEntity findByNumeroOrden(int numeroOrden);
}
