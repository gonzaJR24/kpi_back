package com.medilink.kpi.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "puntaje")
public class Puntaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_puntaje", nullable = false)
    private int id;

    @Column(name = "fecha_evaluacion")
    private LocalDate fechaEvaluacion;

    @ManyToOne
    @JoinColumn(name = "id_empleado")
    @JsonManagedReference
    private Empleado empleado;

    @Column(name = "ausencia_puntualidad", nullable = false)
    private int ausenciaPuntualidad;

    @Column(name = "especifico1", nullable = false)
    private int especifico1;

    @Column(name = "especifico2", nullable = false)
    private int especifico2;

    @Column(name = "nps", nullable = false)
    private int nps;

    @Column(name = "actitudes", nullable = false)
    private int actitudesGestionComportamiento;

    @Column(name = "calificacion_lider", nullable = false)
    private int calificacionLider;

    @Column(name = "puntaje_total", nullable = false)
    private int puntajeTotal;

    @Column(name = "comentario", nullable = false)
    private String comentario;

}
