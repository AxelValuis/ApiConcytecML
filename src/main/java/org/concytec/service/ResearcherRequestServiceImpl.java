package org.concytec.service;

import lombok.RequiredArgsConstructor;
import org.concytec.dto.response.ResearchRequestResponse;
import org.concytec.model.ResearchRequestEntity;
import org.concytec.repository.ResearchRequestRepository;
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

    private final ResearchRequestRepository researchRequestRepository;

    @Override
    public List<ResearchRequestResponse> findAllRequestResearch(Integer page, Integer size, BigInteger postulantId, BigDecimal minimumReliabilityScore) {
        Pageable pageable = new PageRequest(page, size);
        BigDecimal suspiciousProbability = BigDecimal.ONE.subtract(minimumReliabilityScore);
        Page<ResearchRequestEntity> researchRequestEntities = researchRequestRepository
                .findBySuspiciousProbabilityLessThan(suspiciousProbability, pageable);
        return researchRequestEntities.map(ResearchRequestResponse::from).getContent();
    }

    @Override
    public ResearchRequestResponse findRequestResearchByRequestId(int requestId) {
        return null;
    }
}
