package org.concytec.repository;

import org.concytec.model.ResearchEvaluationRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResearchEvaluationRequestRepository extends JpaRepository<ResearchEvaluationRequestEntity, Long> {
    List<ResearchEvaluationRequestEntity> findByIdInvestigador(Long idInvestigador);
}
