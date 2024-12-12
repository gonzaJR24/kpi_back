package com.medilink.kpi.entities;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "empleado")
@AllArgsConstructor
@NoArgsConstructor

public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empleado", nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "id_cargo")
    private Cargo cargo;

    @Column(name = "porcentaje")
    private double porcentaje;

    @Column(name = "monto")
    private double monto;


    @ManyToOne
    @JoinColumn(name="id_area")
    @JsonManagedReference
    private Area area;


    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Puntaje> puntajes;

}

