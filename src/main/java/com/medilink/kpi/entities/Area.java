package com.medilink.kpi.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "area")

public class Area {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_area")
    private Integer id;

    @Column(name = "nombre_area", nullable = false)
    private String nombreArea;

    @Column(name = "cantidad_empleados")
    private Integer cantidadEmpleados;

    @Column(name = "puntaje_total")
    private Integer puntajeTotal;

    @OneToMany(mappedBy = "area", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Empleado>empleados;

    @ManyToOne
    @JoinColumn(name = "id_sucursal", nullable = false)
    private Sucursal sucursal;

//    @Column(name = "rendimiento_area")
//    private double rendimientoArea;
}