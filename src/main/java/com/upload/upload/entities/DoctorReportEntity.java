package com.upload.upload.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "reporte_doctor")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DoctorReportEntity {

  @Id
  @Column(name = "numero_orden")
  private int numeroOrden;

  @Column(name = "fecha_atencion")
  @Temporal(TemporalType.DATE)
  private Date fechaAtencion;

  private int hcu;

  private String descripcion;

  private int costo;

  private String proveedor;

  @Column(name = "mes_entrega")
  private int mes;

  @Column(name = "anio_entrega")
  private int anio;

  @Column(name = "nombre_paciente")
  private String nombrePaciente;
}
