package org.concytec.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "solicitud")
public class SolicitudEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @Column(name = "fecha_creacion")
    private Timestamp fechaSolicitud;

    @Column(name = "estado")
    private String estado;

    @Column(name="ruta_resolucion")
    private String constancia;

    @Column(name="es_observado")
    private Boolean observado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="supervisor_id")
    private UsuarioEntity supervisor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="revisor_id")
    private UsuarioEntity revisor;
}
