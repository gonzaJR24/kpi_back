package com.upload.upload.controllers;

import com.upload.upload.entities.ReceivedDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/received")
@CrossOrigin
public class ReceivedReportsController {

    @PostMapping("/excel")
    public ResponseEntity<?> reportes(@RequestBody ReceivedDTO[] data) throws IOException {
        Workbook libro = new XSSFWorkbook();
        Sheet hoja = libro.createSheet("Hoja 1");

        Row headerRow=hoja.createRow(0);

        Cell nameCell=headerRow.createCell(0);
        nameCell.setCellValue("Name");
        Cell statusCell=headerRow.createCell(1);
        statusCell.setCellValue("Status");

        for(int i=0;i<data.length;i++){
            hoja.autoSizeColumn(0);
            hoja.autoSizeColumn(1);
            Row fila = hoja.createRow(i+1);

            Cell primeraCelda = fila.createCell(0);
            primeraCelda.setCellValue(data[i].name());


            Cell segundaCelda = fila.createCell(1);
            segundaCelda.setCellValue(data[i].status());
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        libro.write(outputStream);
        libro.close();

        byte[] excelContent = outputStream.toByteArray();

        // Set headers for the response
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"reporte.xlsx\"");

        Map<String, byte[]> response=new HashMap<>();
        response.put("response", excelContent);

        return ResponseEntity.ok().headers(headers).body(excelContent);
    }

}
