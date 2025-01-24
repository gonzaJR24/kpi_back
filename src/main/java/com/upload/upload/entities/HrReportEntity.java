package com.upload.upload.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "reporte_produccion")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HrReportEntity {
  @Id
  @Column(name = "numero_orden")
  private int numeroOrden;

  @Column(name = "fecha_facturacion")
  @Temporal(TemporalType.DATE)
  private Date fechaFacturacion;

  private int hcu;

  private String descripcion;

  private String status;

  private int costo;

  private String sucursal;

  private String proveedor;

  @Column(name = "mes_entrega")
  private int mes;

  @Column(name = "anio_entrega")
  private int anio;


}
