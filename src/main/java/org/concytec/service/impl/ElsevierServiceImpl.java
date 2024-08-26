package org.concytec.service.impl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.concytec.exception.GenericClientException;
import org.concytec.model.ResearchEvaluationRequestEntity;
import org.concytec.service.ElsevierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ElsevierServiceImpl implements ElsevierService {

    private static final Logger log = LoggerFactory.getLogger(ElsevierServiceImpl.class);
    private final RestTemplate restTemplate;
    private final Environment environment;

    @Override
    public Double getHIndex(ResearchEvaluationRequestEntity researchEvaluationRequestEntity) {
        String basePath = environment.getProperty("server.url.elsevier");
        String view = environment.getProperty("server.url.elsevier.view");
        String apiKey = environment.getProperty("server.url.elsevier.api-key");
        String token = environment.getProperty("server.url.elsevier.insttoken");

        String url = UriComponentsBuilder.fromHttpUrl(basePath + researchEvaluationRequestEntity.getIdPerfilScopus().toString())
                .queryParam("view", view)
                .queryParam("apiKey", apiKey)
                .queryParam("insttoken", token)
                .toUriString();

        String response = restTemplate.getForObject(url, String.class);
        if (response == null || response.trim().isEmpty()) {
            log.error("Empty response from Elsevier API");
            throw new GenericClientException("INTERNAL_SERVER_ERROR", "Empty response from Elsevier API", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Verifica si la respuesta es un XML de error
        if (response.trim().startsWith("<service-error>")) {
            return handleServiceError(response);
        }

        // En caso de que la respuesta no sea un error, intenta parsearla como JSON
        Map<String, Object> jsonResponse = parseJsonResponse(response);

        // Extrae el valor del h-index
        Map<String, Object> authorRetrievalResponse = (Map<String, Object>) ((List<?>) jsonResponse.get("author-retrieval-response")).get(0);
        String hIndex = (String) authorRetrievalResponse.get("h-index");

        if (hIndex == null) {
            log.error("h-index not found in the API response");
            throw new GenericClientException("NOT_FOUND", "H Index not found", HttpStatus.NOT_FOUND);
        }

        return Double.parseDouble(hIndex); // Convertir h-index a Double
    }

    private Double handleServiceError(String xmlData) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xmlData));
            Document document = builder.parse(is);

            String statusCode = document.getElementsByTagName("statusCode").item(0).getTextContent();
            String statusText = document.getElementsByTagName("statusText").item(0).getTextContent();

            log.error("Elsevier API error: {} - {}", statusCode, statusText);

            if ("INVALID_INPUT".equals(statusCode)) {
                throw new GenericClientException("INVALID_INPUT", "Invalid author ID: " + statusText, HttpStatus.BAD_REQUEST);
            } else if ("RESOURCE_NOT_FOUND".equals(statusCode)) {
                throw new GenericClientException("NOT_FOUND", "The resource specified cannot be found: " + statusText, HttpStatus.NOT_FOUND);
            } else {
                throw new GenericClientException("EXTERNAL_API_ERROR", "An error occurred in the Elsevier API: " + statusText, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            log.error("Error processing XML error response: {}", xmlData, e);
            throw new GenericClientException("INTERNAL_SERVER_ERROR", "Error processing XML error response", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Map<String, Object> parseJsonResponse(String jsonData) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonData, Map.class);
        } catch (Exception e) {
            log.error("Error parsing JSON response: {}", jsonData, e);
            throw new GenericClientException("INTERNAL_SERVER_ERROR", "Error parsing JSON response", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}