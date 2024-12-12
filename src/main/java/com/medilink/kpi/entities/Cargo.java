package com.medilink.kpi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "cargo")
@AllArgsConstructor
@NoArgsConstructor


public class Cargo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cargo", nullable = false)
    private Integer id;

    @Column(name = "nombre_cargo", unique = true)
    private String nombreCargo;

    @OneToMany(mappedBy = "cargo", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Empleado>empleados;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "presupuesto_id")
    private Presupuesto presupuesto;

}