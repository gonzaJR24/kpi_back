package com.upload.upload.services;

import com.upload.upload.entities.DoctorReportEntity;
import com.upload.upload.entities.HrReportEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GenerateJpegService {

    @Autowired
    private DoctorReportService service;

    @Autowired
    private HrReportService hrReportService;

    public ResponseEntity<?> doctor(ArrayList<Integer> numerosOrden) throws IOException {
        List<DoctorReportEntity> entities = new ArrayList<>();
        for (Integer numeroOrden : numerosOrden) {
            DoctorReportEntity entity = service.findByNumeroOrden(numeroOrden);
            entities.add(entity);
        }

        int imageWidth = 1200;
        int imageHeight = 200 + (entities.size() * 30); // Adjust based on the number of rows

        BufferedImage bufferedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        Font font = new Font("Arial", Font.PLAIN, 12);
        graphics.setFont(font);

        this.ImageSettings(graphics, imageWidth, imageHeight);


        //no se puede abstraer
        graphics.setFont(new Font("Arial", Font.BOLD, 14));
        graphics.drawString("ORDEN", 50, 40);
        graphics.drawString("HCU", 150, 40);
        graphics.drawString("FECHA", 250, 40);
        graphics.drawString("DESCRIPCION", 350, 40);
        graphics.drawString("PROVEEDOR", 550, 40);
        graphics.drawString("NOMBRE PACIENTE", 800, 40);

        int y = 70;
        graphics.setFont(font);
        for (DoctorReportEntity entity : entities) {
            graphics.drawString(String.valueOf(entity.getNumeroOrden()), 50, y);
            graphics.drawString(String.valueOf(entity.getHcu()), 150, y);
            graphics.drawString(entity.getFechaAtencion().toString(), 250, y);
            graphics.drawString(entity.getDescripcion(), 350, y);
            graphics.drawString(entity.getProveedor(), 550, y);
            graphics.drawString(entity.getNombrePaciente(), 800, y);
            y += 30;
        }



        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "JPEG", byteArrayOutputStream);

        byte[] imageContent = byteArrayOutputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"reporte_doctor.jpg\"");

        return ResponseEntity.ok().headers(headers).body(imageContent);
    }

    public ResponseEntity<?> produccion(ArrayList<Integer> numerosOrden) throws IOException {
            List<HrReportEntity> entities = new ArrayList<>();
            for (Integer numeroOrden : numerosOrden) {
                HrReportEntity entity = hrReportService.findByNumeroOrden(numeroOrden);
                entities.add(entity);
            }

            int imageWidth = 1200;
            int imageHeight = 200 + (entities.size() * 30); // Adjust based on the number of rows

            BufferedImage bufferedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = bufferedImage.createGraphics();
            this.ImageSettings(graphics, imageWidth, imageHeight);
            Font font = new Font("Arial", Font.PLAIN, 12);
            graphics.setFont(font);

        graphics.setFont(new Font("Arial", Font.BOLD, 14));
            graphics.drawString("ORDEN", 50, 40);
            graphics.drawString("HCU", 150, 40);
            graphics.drawString("DESCRIPCION", 250, 40);
            graphics.drawString("PROVEEDOR", 450, 40);

            int y = 70;
            graphics.setFont(font);
            for (HrReportEntity entity : entities) {
                graphics.drawString(String.valueOf(entity.getNumeroOrden()), 50, y);
                graphics.drawString(String.valueOf(entity.getHcu()), 150, y);
                graphics.drawString(entity.getDescripcion(), 250, y);
                graphics.drawString(entity.getProveedor(), 450, y);
                y += 30;
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "JPEG", byteArrayOutputStream);

            byte[] imageContent = byteArrayOutputStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"reporte_produccion.jpg\"");

            return ResponseEntity.ok().headers(headers).body(imageContent);
    }

    public void ImageSettings(Graphics2D graphics, int imageWidth, int imageHeight){
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, imageWidth, imageHeight); // Fill the background with white
        graphics.setColor(Color.BLACK);
    }

}
