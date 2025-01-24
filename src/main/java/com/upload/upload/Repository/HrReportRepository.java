package com.upload.upload.Repository;

import com.upload.upload.entities.HrReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface HrReportRepository extends JpaRepository<HrReportEntity, Integer> {
  List<HrReportEntity> findByMesAndAnioAndProveedor(int mes, int year, String proveedor);
  HrReportEntity findByNumeroOrden(int numeroOrden);
}
