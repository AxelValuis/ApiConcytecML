package org.concytec.service;

import org.concytec.dto.response.ResearchRequestResponse;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public interface ResearcherRequestService {

    List<ResearchRequestResponse> findAllRequestResearch(Integer page, Integer size, BigInteger postulantId, BigDecimal minimumReliabilityScore);
    ResearchRequestResponse findRequestResearchByRequestId(int requestId);
}
