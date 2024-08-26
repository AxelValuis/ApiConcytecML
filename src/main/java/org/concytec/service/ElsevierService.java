package org.concytec.service;

import lombok.RequiredArgsConstructor;
import org.concytec.exception.GenericClientException;
import org.concytec.model.ResearchEvaluationRequestEntity;
import org.json.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

@Service
@RequiredArgsConstructor
public class ElsevierService {

    private final RestTemplate restTemplate;
    private final Environment environment;

    public double getHIndex(ResearchEvaluationRequestEntity researchEvaluationRequestEntity ) {
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
        if (responseContainsError(response)){
            return researchEvaluationRequestEntity.getIndiceH().doubleValue();
        }
        String hIndexFromXML = getHIndexFromXML(response);
        return Double.parseDouble(hIndexFromXML);
    }

    private boolean responseContainsError(String response) {
        if (response.trim().startsWith("<")) {
            if (response.contains("<statusCode>RESOURCE_NOT_FOUND</statusCode>")) {
                return true;
            }
        }
        return false;
    }

    public static String getHIndexFromXML(String xmlData) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xmlData));
            Document document = builder.parse(is);
            NodeList hIndexNodes = document.getElementsByTagName("h-index");
            if (hIndexNodes.getLength() <= 0) {
               throw new GenericClientException("NOT_FOUND", "H Index not found", HttpStatus.NOT_FOUND);
            }
            return hIndexNodes.item(0).getTextContent();
        } catch (Exception e) {
            throw new GenericClientException("INTERNAL_SERVER_ERROR", "Error processing XML", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
