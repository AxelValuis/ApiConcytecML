package org.concytec.service;

import org.concytec.dto.PredictionResponse;
import org.concytec.model.AuthorData;
import org.concytec.repository.AuthorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PredictionService {

    private final RestTemplate restTemplate;
    private final AuthorRepository authorRepository;
    private final ElsevierService elsevierService;

    private static final Logger logger = LoggerFactory.getLogger(PredictionService.class);

    @Autowired
    public PredictionService(RestTemplate restTemplate, AuthorRepository authorRepository, ElsevierService elsevierService) {
        this.restTemplate = restTemplate;
        this.authorRepository = authorRepository;
        this.elsevierService = elsevierService;
    }

    public PredictionResponse predict(long idInvestigador) {
        // Obtener los datos del autor de la base de datos
        List<AuthorData> authorDataList = authorRepository.findByIdInvestigador(idInvestigador);
        if (authorDataList.isEmpty()) {
            throw new RuntimeException("Author not found");
        }

        AuthorData authorData = authorDataList.get(0);
        double hIndex = (authorData.getIdPerfilScopus() != null && authorData.getIdPerfilScopus() != 0)
                ? elsevierService.getHIndex(String.valueOf(authorData.getIdPerfilScopus()))
                : 0.0;

        // Preparar la solicitud
        Map<String, Object> solicitudInvestigador = new HashMap<>();
        solicitudInvestigador.put("solicitud_investigador", Arrays.asList(
                hIndex,
                authorData.getIdDepartamento(),
                authorData.getEdadInvestigador(),
                authorData.getCantPublicaciones(),
                authorData.getIndicePropInt(),
                authorData.getIndicePublicaciones(),
                authorData.getIdGradoAcademico()
        ));


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(solicitudInvestigador, headers);

        String url = "http://161.132.48.50/predict";

        try {
            logger.info("Sending request to prediction API: {}", request.getBody());
            ResponseEntity<PredictionResponse> response = restTemplate.exchange(url, HttpMethod.POST, request, PredictionResponse.class);

            if (response.getBody() != null) {
                logger.info("Received response from prediction API: {}", response.getBody());
                return response.getBody();
            } else {
                logger.error("Received null response from prediction API");
                return new PredictionResponse();
            }

        } catch (HttpStatusCodeException e) {
            logger.error("HTTP error occurred: {}", e.getStatusCode());
            throw new RuntimeException("Prediction API failed with status: " + e.getStatusCode());
        } catch (Exception e) {
            logger.error("An error occurred while calling the prediction API", e);
            throw new RuntimeException("Failed to get prediction response", e);
        }
    }
}
