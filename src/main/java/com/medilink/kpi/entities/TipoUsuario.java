package com.medilink.kpi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jdk.dynalink.linker.LinkerServices;
import lombok.*;

import java.util.List;

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

    @JsonIgnore
    @OneToMany(mappedBy = "tipoUsuario") // Define el lado inverso de la relación
    private List<Usuario> usuario;

}
