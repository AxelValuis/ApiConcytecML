package org.concytec.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "tbl_hechos_ml")
public class ResearchEvaluationRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long solicitudId;

    @Column(name = "postulante_id")
    private Long postulanteId;

    @Column(name = "id_investigador")
    private Long idInvestigador;

    @Column(name = "id_perfil_scopus")
    private Long idPerfilScopus;

    @Column(name = "indice_h")
    private Long indiceH;

    @Column(name = "id_departamento")
    private Long idDepartamento;

    @Column(name = "edad_investigador")
    private Long edadInvestigador;

    @Column(name = "cant_publicaciones")
    private Long cantPublicaciones;

    @Column(name = "indice_prop_int")
    private Double indicePropInt;

    @Column(name = "indice_publicaciones")
    private Double indicePublicaciones;

    @Column(name = "id_grado_academico")
    private Double idGradoAcademico;
}
