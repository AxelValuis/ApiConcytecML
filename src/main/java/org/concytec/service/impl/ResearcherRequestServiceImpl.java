package org.concytec.service.impl;

import lombok.RequiredArgsConstructor;
import org.concytec.dto.response.ResearchRequestResponse;
import org.concytec.model.ResearchRiskPredictionEntity;
import org.concytec.repository.ResearchRiskPredictionRepository;
import org.concytec.service.ResearcherRequestService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResearcherRequestServiceImpl implements ResearcherRequestService {

    private final ResearchRiskPredictionRepository researchRiskPredictionRepository;

    @Override
    public List<ResearchRequestResponse> findAllRequestResearch(Integer page, Integer size, BigInteger postulantId, BigDecimal minimumReliabilityScore) {
        Pageable pageable = new PageRequest(page, size);
        BigDecimal suspiciousProbability = BigDecimal.ONE.subtract(minimumReliabilityScore);

        Page<ResearchRiskPredictionEntity> researchRequestEntities;
        if (postulantId != null) {
            researchRequestEntities = researchRiskPredictionRepository
                    .findBySuspiciousProbabilityLessThanAndPostulantId(suspiciousProbability, postulantId, pageable);
        } else {
            researchRequestEntities = researchRiskPredictionRepository
                    .findBySuspiciousProbabilityLessThan(suspiciousProbability, pageable);
        }

        return researchRequestEntities.map(ResearchRequestResponse::from).getContent();
    }


    @Override
    public ResearchRequestResponse findRequestResearchByRequestId(int requestId) {
        return null;
    }
}
