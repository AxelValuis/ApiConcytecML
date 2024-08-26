package org.concytec.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "postulante")
public class PostulanteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @Column(name = "desc_tipo_documento")
    private String tipodoc;

    @Column(name = "nro_documento")
    private String numdoc;

    @Column(name = "nombres")
    private String nombres;

    @Column(name= "apellido_paterno")
    private String apellidoPaterno;

    @Column(name = "apellido_materno")
    private String apellidoMaterno;

    @Column(name = "email")
    private String correo;
}
