package com.medilink.kpi.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario", nullable = false)
    private Integer id;

    @Column(name = "fecha_creacion")
    private LocalDate fechaCreacion;

    @Column(name = "nombres", nullable = false)
    private String nombres;

    @Column(name = "apellidos", nullable = false)
    private String apellidos;

    @Column(name = "usuario", nullable = false, unique = true)
    private String nombre_usuario;

    @Column(name = "contrasena", nullable = false)
    private String contrasena;

    @ManyToOne
    @JoinColumn(name="id_tipo_usuario", nullable = false)
    private TipoUsuario tipoUsuario;

    @ManyToOne
    @JoinColumn(name = "id_sucursal",  nullable = false)
    private Sucursal sucursal;


}