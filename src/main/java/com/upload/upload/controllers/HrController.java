package com.upload.upload.controllers;

import com.upload.upload.entities.DateDTO;
import com.upload.upload.entities.HrReportEntity;
import com.upload.upload.services.HrReportService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/recursos_humanos")
@CrossOrigin
public class HrController {

  public Cell orderCell;
  public Cell fechaFacturaCell;
  public Cell hcuCell;
  public Cell grupoCell;
  public Cell atendidoCell;
  public Cell costoCell;
  public Cell sucursalCell;
  public Cell proveedorCell;

  @Autowired
  private HrReportService reportService;


  @PostMapping("/hr_upload")
  public ResponseEntity<?> uploadExcel(@RequestParam("file") MultipartFile file, @RequestParam int mes, @RequestParam int anio) throws IOException {

  return ResponseEntity.ok(reportService.generate(file, mes, anio));
  }

  @GetMapping
  public List<HrReportEntity> list(){
    return reportService.list();
  }

  @PostMapping("/searchByDate")
  public List<HrReportEntity> listByDate(@RequestBody DateDTO dateDTO){
    return reportService.findByFechaAndProveedor(dateDTO.month(), dateDTO.year(), dateDTO.proveedor());
  }

  @DeleteMapping
  public void delete(){
    reportService.deleteAll();
  }
}



