package com.medilink.kpi.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "presupuesto")
public class Presupuesto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "presupuesto_id", nullable = false)
    private int id;

    @Column(name = "monto_kpi")
    private double montoKpi;


    @Column(name = "fecha(yyyy-mm-dd)")
    LocalDate date=LocalDate.now();

    @JsonBackReference
    @OneToMany(mappedBy = "presupuesto", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Cargo>cargos;
}
