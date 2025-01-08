package com.medilink.kpi.Controllers;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DottedBorder;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.medilink.kpi.entities.dto.PdfDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;
import java.io.ByteArrayOutputStream;



@RestController
@CrossOrigin
public class PdfController {

    @PostMapping("/view-pdf")
    public ResponseEntity<byte[]> viewPdf(@RequestBody PdfDTO pdfDTO) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document doc = new Document(pdfDoc);

            String imagePath = "https://github.com/gonzaJR24/kpi_front/blob/main/Logo-fondomorado.png?raw=true"; // Replace with your image path
            ImageData imageData = ImageDataFactory.create(imagePath);
            Image image = new Image(imageData);
            image.setWidth(250);
            image.setHeight(100);
            image.setMarginBottom(34);

            image.setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.CENTER);

            doc.add(image);

            // Configuración del contenido
            Color bgColour = new DeviceRgb(173, 216, 230);
            Color bgYellowColour = new DeviceRgb(255, 255, 0);

            Paragraph p1 = new Paragraph("Informe Evaluacion Mensual");
            p1.setTextAlignment(TextAlignment.CENTER);
            p1.setBackgroundColor(bgColour);
            p1.setFontSize(15);
            p1.setUnderline();
//            p1.setBold();
            doc.add(p1);

            Paragraph p2 = new Paragraph("Nombre Colaborador: " + pdfDTO.nombreEmpleado());
            p2.setFontSize(10);
//            p2.setBackgroundColor(bgColour);
//            p2.setTextAlignment(TextAlignment.CENTER);
            doc.add(p2);

            Paragraph p3 = new Paragraph("Area: " + pdfDTO.area());
            p3.setFontSize(10);
//            p3.setBackgroundColor(bgColour);
//            p3.setTextAlignment(TextAlignment.CENTER);
            doc.add(p3);

            Paragraph p4 = new Paragraph("Cargo: " + pdfDTO.cargo());
            p4.setFontSize(10);
//            p4.setBackgroundColor(bgColour);
//            p4.setTextAlignment(TextAlignment.CENTER);
            doc.add(p4);

            Paragraph p5 = new Paragraph("Periodo: " + pdfDTO.mes());
            p5.setFontSize(10);
//            p5.setBackgroundColor(bgColour);
//            p5.setTextAlignment(TextAlignment.CENTER);
            doc.add(p5);

            Paragraph p6 = new Paragraph("Lider Inmediato: " + pdfDTO.lider());
            p6.setFontSize(10);
//            p6.setBackgroundColor(bgColour);
//            p6.setTextAlignment(TextAlignment.CENTER);
            doc.add(p6);

            Paragraph p7 = new Paragraph("Calificacion Lider: " + pdfDTO.calificacionLider());
            p7.setFontSize(10);
//            p7.setBackgroundColor(bgColour);
//            p7.setTextAlignment(TextAlignment.CENTER);
            doc.add(p7);

            Paragraph p8 = new Paragraph("Monto final: " + pdfDTO.montofinal());
            p8.setFontSize(10);
//            p8.setBackgroundColor(bgColour);
//            p8.setTextAlignment(TextAlignment.CENTER);
            doc.add(p8);

//            Paragraph p9 = new Paragraph("Comentario: ");
//            p9.setFontSize(12);
//            p9.setBackgroundColor(bgColour);
//            p9.setTextAlignment(TextAlignment.CENTER);
//            doc.add(p9);

            Paragraph p10 = new Paragraph(pdfDTO.comentario());
            p10.setFontSize(10);
            p10.setBorder(new SolidBorder(1));
            p10.setBackgroundColor(bgColour);
            p10.setPadding(20);
            p10.setMarginTop(10);
//            p10.setTextAlignment(TextAlignment.CENTER);
            doc.add(p10);

            Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
            table.setMarginTop(50);

            Cell firmaColaborador = new Cell();
            firmaColaborador.add(new Paragraph("Firma del Colaborador:").setFontSize(10));
            firmaColaborador.setTextAlignment(TextAlignment.CENTER);
            firmaColaborador.setPaddingTop(20);
            firmaColaborador.setUnderline();
            firmaColaborador.setBorder(null);
            table.addCell(firmaColaborador);

            Cell firmaLider = new Cell();
            firmaLider.add(new Paragraph("Firma del Líder:").setFontSize(10));
            firmaLider.setTextAlignment(TextAlignment.CENTER);
            firmaLider.setPaddingTop(20);
            firmaLider.setUnderline();
            firmaLider.setBorder(null);
            table.addCell(firmaLider);

            doc.add(table);


            Paragraph p11 = new Paragraph("""
                    Este documento es de uso interno de la empresa MediLink srl. Por lo tanto, es prohibida su divulgación externa, la información que contine no representa, incremento salarial o ascenso de cargo, es un instrumento usado para evaluar el desempeño y productividad en forma global e individual de los colaboradores.""");
            p11.setMarginTop(60);
            p11.setTextAlignment(TextAlignment.CENTER);
            p11.setBackgroundColor(bgYellowColour);
            p11.setFontSize(10);
            doc.add(p11);



            doc.close();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=informe.pdf");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el PDF", e);
        }
    }
}
