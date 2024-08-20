package org.concytec.repository;

import org.concytec.model.ResearchRequestEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;

@Repository
public interface ResearchRequestRepository extends JpaRepository<ResearchRequestEntity, BigInteger> {
    Page<ResearchRequestEntity> findBySuspiciousProbabilityLessThanAndPostulantId(BigDecimal suspiciousProbability,
                                                                                  BigInteger postulantId,
                                                                                  Pageable pageable);
    Page<ResearchRequestEntity> findBySuspiciousProbabilityLessThan(BigDecimal suspiciousProbability,
                                                                                  Pageable pageable);
    Long countBySuspiciousProbabilityLessThanAndPostulantId(BigDecimal suspiciousProbability,
                                                            BigInteger postulantId);
}
