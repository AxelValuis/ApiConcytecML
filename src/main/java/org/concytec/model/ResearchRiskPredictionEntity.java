package org.concytec.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_predict_ml")
public class ResearchRiskPredictionEntity {

    @Id
    @Column(name = "solicitud_id")
    private BigInteger requestId;

    @Column(name = "postulante_id")
    private BigInteger postulantId;

    @Column(name = "id_investigador")
    private BigInteger researcherId;

    @Column(name = "prob_sospechoso")
    private BigDecimal suspiciousProbability;

    @Column(name = "var_import")
    @Convert(converter = JsonBigDecimalListConverter.class)
    private List<BigDecimal> criteriaEvaluatedList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitud_id", insertable = false, updatable = false)
    private SolicitudEntity solicitud;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "postulante_id", insertable = false, updatable= false)
    private PostulanteEntity postulante;
}
