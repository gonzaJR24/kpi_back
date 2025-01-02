package com.medilink.kpi.Controllers;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.Background;
import com.itextpdf.layout.properties.TextAlignment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;

@RestController
public class PdfController {

    @GetMapping("/view-pdf")
    public ResponseEntity<byte[]> viewPdf() {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // Crear PDF en memoria
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document doc = new Document(pdfDoc);

            // Configuración del contenido
            Color bgColour = new DeviceRgb(173, 216, 230);

            Paragraph p1 = new Paragraph("Informe Evaluacion Mensual");
            p1.setTextAlignment(TextAlignment.CENTER);
            p1.setFontSize(14);
            p1.setUnderline();
            doc.add(p1);

            Paragraph p2 = new Paragraph("Nombre Colaborador");
            p2.setFontSize(10);
            p2.setBackgroundColor(bgColour);
            doc.add(p2);

            Paragraph p3 = new Paragraph("Area");
            p3.setFontSize(10);
            p3.setBackgroundColor(bgColour);
            doc.add(p3);

            Paragraph p4 = new Paragraph("Cargo");
            p4.setFontSize(10);
            p4.setBackgroundColor(bgColour);
            doc.add(p4);

            Paragraph p5 = new Paragraph("Periodo");
            p5.setFontSize(10);
            p5.setBackgroundColor(bgColour);
            doc.add(p5);

            Paragraph p6 = new Paragraph("Lider Inmediato");
            p6.setFontSize(10);
            p6.setBackgroundColor(bgColour);
            doc.add(p6);

            doc.close();

            // Configurar encabezados para abrir el PDF en el navegador
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=sample_pdf.pdf");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el PDF", e);
        }
    }
}
