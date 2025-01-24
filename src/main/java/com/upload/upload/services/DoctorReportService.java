package com.upload.upload.services;

import com.upload.upload.Repository.DoctorReportRepository;
import com.upload.upload.entities.DoctorReportEntity;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DoctorReportService {

  @Autowired
  private DoctorReportRepository repository;

  public void save(DoctorReportEntity entity){
    repository.save(entity);
  }

  public List<DoctorReportEntity> list(){
    return repository.findAll();
  }

  public List<DoctorReportEntity> findByDate(int month, int year, String proveedor){
    return repository.findByMesAndAnioAndProveedor(month, year, proveedor);
  }

  public void delete(){
    repository.deleteAll();
  }

  public DoctorReportEntity findByNumeroOrden(int numeroOrden){
    return repository.findByNumeroOrden(numeroOrden);
  }



  public List<DoctorReportEntity> generateReport(MultipartFile file, String name, int mes, int anio) throws IOException {
    List<DoctorReportEntity> entities = new ArrayList<>();

    Workbook workbook = new XSSFWorkbook(file.getInputStream());
    Sheet sheet = workbook.getSheetAt(0);

    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
      Row row = sheet.getRow(i);
      if (row == null) {
        continue;
      }

      DoctorReportEntity entity = new DoctorReportEntity();

      Cell hcuCell = row.getCell(1);
      Cell nombreCell = row.getCell(2);
      Cell orderCell = row.getCell(3);
      Cell descripcionCell = row.getCell(4);
      Cell dateCell = row.getCell(10);
      Cell costoCell = row.getCell(12);


      if (orderCell != null && orderCell.getCellType() == CellType.NUMERIC) {
        entity.setNumeroOrden((int) orderCell.getNumericCellValue());
      }

      if (hcuCell != null && hcuCell.getCellType() == CellType.NUMERIC) {
        entity.setHcu((int) hcuCell.getNumericCellValue());
      }

      if (dateCell != null) {
        if (dateCell.getCellType() == CellType.STRING) {
          String dateString = dateCell.getStringCellValue().trim();
          try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = sdf.parse(dateString);
            entity.setFechaAtencion(new java.sql.Date(date.getTime()));
          } catch (ParseException e) {
            System.err.println("Error parsing date: " + e.getMessage());
          }
        } else if (dateCell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(dateCell)) {
          Date date = dateCell.getDateCellValue();
          entity.setFechaAtencion(new java.sql.Date(date.getTime()));
        }
      }

      if (descripcionCell != null && descripcionCell.getCellType() == CellType.STRING) {
        entity.setDescripcion(descripcionCell.getStringCellValue());
      }

      if (nombreCell != null && nombreCell.getCellType() == CellType.STRING) {
        entity.setNombrePaciente(nombreCell.getStringCellValue());
      }

      if (costoCell != null && costoCell.getCellType() == CellType.NUMERIC) {
        entity.setCosto((int) costoCell.getNumericCellValue());
      }

      entity.setProveedor(name);

      entity.setMes(mes);
      entity.setAnio(anio);

      entities.add(entity);
      save(entity);
    }

    return entities;
  }
}
