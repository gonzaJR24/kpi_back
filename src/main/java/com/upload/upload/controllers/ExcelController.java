package com.upload.upload.controllers;

import com.upload.upload.entities.DoctorDateDTO;
import com.upload.upload.entities.DoctorReportEntity;
import com.upload.upload.services.DoctorReportService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class ExcelController {

  @Autowired
  private DoctorReportService service;

  @GetMapping
  public List<DoctorReportEntity> list(){
    return service.list();
  }

  @PostMapping("/upload-excel")
  public ResponseEntity<List<DoctorReportEntity>> uploadExcel(@RequestParam("file") MultipartFile file, @RequestParam String name, @RequestParam int mes, @RequestParam int anio) throws IOException {
    return ResponseEntity.ok(service.generateReport(file, name, mes, anio));
  }

  @PostMapping("/searchByDate")
  public List<DoctorReportEntity> findByDate(@RequestBody DoctorDateDTO doctorDateDTO){
    return service.findByDate(doctorDateDTO.month(), doctorDateDTO.year(), doctorDateDTO.proveedor());
  }

  @DeleteMapping
  public void delete(){
    service.delete();
  }
}


