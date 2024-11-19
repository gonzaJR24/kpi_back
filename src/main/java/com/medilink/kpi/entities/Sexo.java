package com.medilink.kpi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "sexo")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Sexo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sexo", nullable = false)
    private int id;

    @Column(nullable = false, unique = true)
    private String sexo;

    @OneToMany(mappedBy = "sexo", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Empleado>empleados;
}
