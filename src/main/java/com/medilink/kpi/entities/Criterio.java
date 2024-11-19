package com.medilink.kpi.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "criterio")
public class Criterio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_criterio", nullable = false)
    private Integer id;

    @Column(name = "nombre_criterio", unique = true, nullable = false)
    private String nombreCriterio;

    @Column(nullable = false)
    private Integer valor;

    @ManyToOne
    @JoinColumn(name = "id_area", nullable = false)
    @JsonManagedReference
    private Area area;
}
