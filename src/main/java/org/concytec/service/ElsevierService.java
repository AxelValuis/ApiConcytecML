package org.concytec.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONObject;

@Service
public class ElsevierService {

    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(ElsevierService.class); // Declarar el logger

    @Autowired
    public ElsevierService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public double getHIndex(String authorId) {
        String url = "https://api.elsevier.com/content/author/author_id/" + authorId +
                "?view=ENHANCED&apiKey=69938da9607d1b0a4084a58332abbd95&insttoken=2702f4b17a35eab090ef9f70fd64d8d1";

        logger.info("Calling Elsevier API with URL: {}", url);

        String response = restTemplate.getForObject(url, String.class);

        logger.info("Received response from Elsevier API: {}", response);

        try {
            // Verificar si la respuesta es JSON
            if (response.trim().startsWith("{")) {
                JSONObject jsonResponse = new JSONObject(response);
                JSONObject authorRetrievalResponse = jsonResponse.getJSONArray("author-retrieval-response").getJSONObject(0);
                if (authorRetrievalResponse.has("h-index")) {
                    String hIndexStr = authorRetrievalResponse.getString("h-index");
                    return Double.parseDouble(hIndexStr);
                } else {
                    logger.error("h-index is not available for authorId {}", authorId);
                    throw new RuntimeException("h-index not found in Elsevier API response");
                }
            } else {
                logger.error("Received unexpected format from Elsevier API for authorId {}", authorId);
                throw new RuntimeException("Unexpected response format from Elsevier API");
            }
        } catch (Exception e) {
            logger.error("Failed to parse h-index from Elsevier API response", e);
            throw new RuntimeException("Failed to parse h-index from Elsevier API response", e);
        }
    }

}
