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
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResearchRequestResponse {
    private BigInteger requestId;
    private PostulantDTO postulant;
    private BigDecimal reliabilityScore;
    private List<CriteriaEvaluatedDTO> criteriaEvaluatedList;

    public static ResearchRequestResponse from(ResearchRiskPredictionEntity entity){
        BigDecimal reliabilityScore = entity.getSuspiciousProbability();
        PostulantDTO postulantDTO = PostulantDTO.create(entity.getPostulantId(), entity.getResearcherId());
        List<CriteriaEvaluatedDTO> criteriaEvaluatedDTOList =
                IntStream.range(0, entity.getCriteriaEvaluatedList().size())
                        .mapToObj(index -> CriteriaEvaluatedDTO.create(ResearcherAttribute.fromCode(index).getDescription()
                                , entity.getCriteriaEvaluatedList().get(index)))
                        .collect(Collectors.toList());

        return new ResearchRequestResponse(entity.getRequestId(), postulantDTO, reliabilityScore, criteriaEvaluatedDTOList);

    }
}
