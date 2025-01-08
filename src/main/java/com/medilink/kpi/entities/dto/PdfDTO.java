package com.medilink.kpi.entities.dto;

public record PdfDTO(String nombreEmpleado, String area, String cargo, String mes, String lider,
                     int calificacionLider, float montofinal, String comentario) {
}
