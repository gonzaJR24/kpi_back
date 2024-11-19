package com.medilink.kpi.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tipo_usuario")
public class TipoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_usuario", nullable = false)
    private int idTipoUsuario;

    @Column(name = "tipo_usuario", nullable = false, unique = true)
    private String tipoUsuario;
}
