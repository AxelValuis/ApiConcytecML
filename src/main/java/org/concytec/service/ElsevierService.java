package org.concytec.service;

import org.concytec.model.ResearchEvaluationRequestEntity;

public interface ElsevierService {
    Double getHIndex(ResearchEvaluationRequestEntity researchEvaluationRequestEntity);
}
