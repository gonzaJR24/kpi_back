package com.upload.upload.controllers;
import com.upload.upload.services.GenerateJpegService;
import com.upload.upload.services.HrReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.*;
import java.util.ArrayList;


@RestController
@CrossOrigin("*")
@RequestMapping("/generate")
public class GenerateReportController {

    @Autowired
    private GenerateJpegService service;

    @Autowired
    private HrReportService hrReportService;

    @PostMapping("/doctor")
    public ResponseEntity<?> generarDoctor(@RequestBody ArrayList<Integer> numerosOrden) throws IOException {
        return service.doctor(numerosOrden);
    }


    @PostMapping("/produccion")
    public ResponseEntity<?> generarProduccion(@RequestBody ArrayList<Integer> numerosOrden) throws IOException {
        return service.produccion(numerosOrden);
    }
}
