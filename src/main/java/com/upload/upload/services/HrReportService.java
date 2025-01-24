package com.upload.upload.services;

import com.upload.upload.Repository.HrReportRepository;
import com.upload.upload.entities.DoctorReportEntity;
import com.upload.upload.entities.HrReportEntity;
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
import java.util.Date;
import java.util.List;

@Service
public class HrReportService {

  public Cell orderCell;
  public Cell fechaFacturaCell;
  public Cell hcuCell;
  public Cell grupoCell;
  public Cell atendidoCell;
  public Cell costoCell;
  public Cell sucursalCell;
  public Cell proveedorCell;

  @Autowired
  private HrReportRepository repository;

  public void save(HrReportEntity entity){
    repository.save(entity);
  }

  public List<HrReportEntity>list(){
    return repository.findAll(Sort.by(Sort.Direction.ASC, "proveedor"));
  }

  public List<HrReportEntity> findByFechaAndProveedor(int month,int year, String proveedor){
    return repository.findByMesAndAnioAndProveedor(month, year, proveedor);
  }

  public void deleteAll(){
      repository.deleteAll();
  }

  public HrReportEntity findByNumeroOrden(int numeroOrden){
    return repository.findByNumeroOrden(numeroOrden);
  }

  public ResponseEntity<?> generate(MultipartFile file, int mes, int anio){

    try{
      Workbook workbook = new XSSFWorkbook(file.getInputStream());
      Sheet sheet = workbook.getSheetAt(0);
      for (int i = 1; i <= sheet.getLastRowNum(); i++) {
        Row row = sheet.getRow(i);
        if (row == null) {
          continue;
        }

        HrReportEntity entity = new HrReportEntity();

        for(Cell cell:sheet.getRow(0)){
          switch (cell.getStringCellValue()){
            case "ORDEN":
              orderCell=row.getCell(cell.getColumnIndex());
            case "FECHA FACTURA":
              fechaFacturaCell=row.getCell(cell.getColumnIndex());
            case "H.C.U.":
              hcuCell=row.getCell(cell.getColumnIndex());
            case "GRUPO":
              grupoCell=row.getCell(cell.getColumnIndex());
            case "ATENDIDO":
              atendidoCell=row.getCell(cell.getColumnIndex());
            case "COSTO":
              costoCell=row.getCell(cell.getColumnIndex());
            case "SUCURSAL":
              sucursalCell=row.getCell(cell.getColumnIndex());
            case "PROVEEDOR":
              proveedorCell=row.getCell(cell.getColumnIndex());
          }
        }


        if (orderCell != null) {
          if (orderCell.getCellType() == CellType.NUMERIC) {
            entity.setNumeroOrden((int) orderCell.getNumericCellValue());
          }
        }

        if (fechaFacturaCell != null) {
          if (fechaFacturaCell.getCellType() == CellType.STRING) {
            String dateString = fechaFacturaCell.getStringCellValue().trim();
            try {
              SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
              Date date = sdf.parse(dateString);
              entity.setFechaFacturacion(new java.sql.Date(date.getTime()));
            } catch (ParseException e) {
              System.err.println("Error al parsear la fecha: " + e.getMessage());
            }
          } else if (fechaFacturaCell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(fechaFacturaCell)) {
            Date date = fechaFacturaCell.getDateCellValue();
            entity.setFechaFacturacion(new java.sql.Date(date.getTime()));
          }
        }

        if (hcuCell != null) {
          if (hcuCell.getCellType() == CellType.NUMERIC) {
            entity.setHcu((int) hcuCell.getNumericCellValue());
          }
        }

        if (grupoCell != null && grupoCell.getCellType() == CellType.STRING) {
          entity.setDescripcion(grupoCell.getStringCellValue());
        }else{
          continue;
        }

        if (atendidoCell != null && atendidoCell.getCellType() == CellType.STRING) {
          entity.setStatus(atendidoCell.getStringCellValue());
        }

        if (costoCell != null) {
          if (costoCell.getCellType() == CellType.NUMERIC) {
            entity.setCosto((int) costoCell.getNumericCellValue());
          }
        }

        if (sucursalCell != null && sucursalCell.getCellType() == CellType.STRING) {
          entity.setSucursal(sucursalCell.getStringCellValue());
        }


        if (proveedorCell != null && proveedorCell.getCellType() == CellType.STRING) {
          entity.setProveedor(proveedorCell.getStringCellValue());
        }

        entity.setMes(mes);
        entity.setAnio(anio);


        if(entity.getProveedor().isEmpty()){
          continue;
        }else{
          save(entity);
        }
      }
    } catch (OutOfMemoryError e) {
      return ResponseEntity.status(404).body(e.getMessage());
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
      return ResponseEntity.ok(list());
  }
}
