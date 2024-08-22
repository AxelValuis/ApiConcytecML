package org.concytec.repository;

import org.concytec.model.AuthorData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepository extends JpaRepository<AuthorData, Long> {
    List<AuthorData> findByIdInvestigador(Long idInvestigador);
}
