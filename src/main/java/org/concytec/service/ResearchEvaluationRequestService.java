package org.concytec.service;

import org.concytec.dto.ResearchEvaluationRequestResponse;

public interface ResearchEvaluationRequestService {
    ResearchEvaluationRequestResponse predict(Long idInvestigador);
}
