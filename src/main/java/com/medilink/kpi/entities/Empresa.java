package com.medilink.kpi.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "empresa")
public class Empresa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empresa")
    private int idEmpresa;

    @Column(name = "nombre_empresa" , unique = true)
    private String nombreEmpresa;

    @Column(name = "valor_real_mes", nullable = false)
    private double progresoEmpresa;

    @Column(name = "valor_meta_mes", nullable = false)
    private double valorMeta;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    List<Sucursal>sucursal;
    
}
