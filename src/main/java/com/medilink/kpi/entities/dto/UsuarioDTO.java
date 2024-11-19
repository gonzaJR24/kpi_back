package com.medilink.kpi.entities.dto;

public record UsuarioDTO(String nombres, String apellidos, String nombreUsuario, String contrasena, String correo,
                         int tipoUsuario, int sucursal) {
}
