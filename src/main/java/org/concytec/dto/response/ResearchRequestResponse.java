package org.concytec.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.concytec.dto.CriteriaEvaluatedDTO;
import org.concytec.dto.PostulantDTO;
import org.concytec.enums.ResearcherAttribute;
import org.concytec.model.ResearchRiskPredictionEntity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResearchRequestResponse {
    private BigInteger requestId;
    private String tipodoc;
    //private PostulantDTO postulant;
    private String numdoc;
    private String nombres;
    private String correo;
    private String supervisor;
    private String revisor;
    private Timestamp fechaSolicitud;
    private String estado;
    private String constancia;
    private Boolean observado;

    private BigDecimal reliabilityScore;
    private List<CriteriaEvaluatedDTO> criteriaEvaluatedList;


    public static ResearchRequestResponse from(ResearchRiskPredictionEntity entity){
        return ResearchRequestResponse.builder()
                .requestId(entity.getRequestId())
                .tipodoc(entity.getPostulante().getTipodoc())
                .numdoc(entity.getPostulante().getNumdoc())
                .nombres(entity.getPostulante().getNombres() + " " + entity.getPostulante().getApellidoPaterno() + " " + entity.getPostulante().getApellidoMaterno())
                .correo(entity.getPostulante().getCorreo())
                .supervisor(entity.getSolicitud().getSupervisor().getNombres())
                .revisor(entity.getSolicitud().getRevisor().getNombres())
                .fechaSolicitud(entity.getSolicitud().getFechaSolicitud())
                .estado(entity.getSolicitud().getEstado())
                .constancia(entity.getSolicitud().getConstancia())
                .observado(entity.getSolicitud().getObservado())
                .reliabilityScore(entity.getSuspiciousProbability())
                .criteriaEvaluatedList(IntStream.range(0, entity.getCriteriaEvaluatedList().size())
                        .mapToObj(index -> CriteriaEvaluatedDTO.create(ResearcherAttribute.fromCode(index).getDescription(), entity.getCriteriaEvaluatedList().get(index)))
                        .collect(Collectors.toList()))
                .build();
    }



}
