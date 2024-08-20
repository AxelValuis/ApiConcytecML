package org.concytec.controller;

import lombok.RequiredArgsConstructor;
import org.concytec.dto.response.ResearchRequestResponse;
import org.concytec.service.ResearcherRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/research")
@RequiredArgsConstructor
public class ResearchRequestController {

    private final ResearcherRequestService researcherRequestService;

    @GetMapping("/requests")
    public List<ResearchRequestResponse> findAllRequestResearch(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false) BigInteger postulantId,
            @RequestParam(required = false, defaultValue = "0.00") BigDecimal minimumReliabilityScore) {
        return researcherRequestService.findAllRequestResearch(page, size, postulantId, minimumReliabilityScore);
    }

    @GetMapping("/requests/{requestId}")
    public ResearchRequestResponse findRequestResearchByRequestId(@PathVariable BigInteger requestId) {
        return null;
    }
}
