package org.concytec.controller;

import lombok.RequiredArgsConstructor;
import org.concytec.dto.ResearchEvaluationRequestResponse;
import org.concytec.service.ResearchEvaluationRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/research/request/evaluation")
@RequiredArgsConstructor
public class ResearchEvaluationRequestController {
    private final ResearchEvaluationRequestService researchEvaluationRequestService;

    @GetMapping("/{researcherId}")
    public ResponseEntity<ResearchEvaluationRequestResponse> predict(@PathVariable long researcherId) {
        ResearchEvaluationRequestResponse response = researchEvaluationRequestService.predict(researcherId);
        return ResponseEntity.ok(response);
    }
}
