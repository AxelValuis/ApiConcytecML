package org.concytec.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.concytec.dto.ResearchEvaluationRequestResponse;
import org.concytec.exception.GenericClientException;
import org.concytec.model.ResearchEvaluationRequestEntity;
import org.concytec.repository.ResearchEvaluationRequestRepository;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResearchEvaluationRequestServiceImpl implements ResearchEvaluationRequestService{

    private final RestTemplate restTemplate;
    private final ResearchEvaluationRequestRepository researchEvaluationRequestRepository;
    private final ElsevierService elsevierService;
    private final Environment environment;

    public ResearchEvaluationRequestResponse predict(Long researcherId) {
        List<ResearchEvaluationRequestEntity> researchEvaluationRequestEntityList = researchEvaluationRequestRepository.findByIdInvestigador(researcherId);
        if (researchEvaluationRequestEntityList.isEmpty()) {
            throw GenericClientException.create("NOT_FOUND", "Author not found", HttpStatus.NOT_FOUND);
        }

        ResearchEvaluationRequestEntity researchEvaluationRequestEntity = researchEvaluationRequestEntityList.get(0);
        Double hIndex = (researchEvaluationRequestEntity.getIdPerfilScopus() != null && researchEvaluationRequestEntity.getIdPerfilScopus() != 0)
                ? elsevierService.getHIndex(researchEvaluationRequestEntity)
                : researchEvaluationRequestEntity.getIndiceH();


        Map<String, Object> solicitudInvestigador = new HashMap<>();
        solicitudInvestigador.put("solicitud_investigador", Arrays.asList(
                hIndex,
                researchEvaluationRequestEntity.getIdDepartamento(),
                researchEvaluationRequestEntity.getEdadInvestigador(),
                researchEvaluationRequestEntity.getCantPublicaciones(),
                researchEvaluationRequestEntity.getIndicePropInt(),
                researchEvaluationRequestEntity.getIndicePublicaciones(),
                researchEvaluationRequestEntity.getIdGradoAcademico()
        ));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(solicitudInvestigador, headers);

        String url = environment.getProperty("server.url.predict-ml");

        log.info("Sending request to prediction API: {}", request.getBody());
        ResponseEntity<ResearchEvaluationRequestResponse> response = restTemplate.exchange(url, HttpMethod.POST, request, ResearchEvaluationRequestResponse.class);
        if(response.getStatusCode().is5xxServerError() || response.getStatusCode().is4xxClientError()){
            throw new GenericClientException("INTERNAL_SERVER_ERROR", "Prediction API has error unexpected", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response.getBody();
    }
}
