package org.concytec.controller;

import org.concytec.dto.PredictionResponse;
import org.concytec.service.ElsevierService;
import org.concytec.service.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/predict")
public class PredictionController {

    private static final Logger logger = LoggerFactory.getLogger(PredictionController.class);

    private final ElsevierService elsevierService;
    private final PredictionService predictionService;

    @Autowired
    public PredictionController(ElsevierService elsevierService, PredictionService predictionService) {
        this.elsevierService = elsevierService;
        this.predictionService = predictionService;
    }

    @GetMapping("/{idInvestigador}")
    public ResponseEntity<PredictionResponse> predict(@PathVariable long idInvestigador) {
        try {
            logger.info("Received request to predict for idInvestigador: {}", idInvestigador);

            // Realiza la predicción
            PredictionResponse response = predictionService.predict(idInvestigador);

            if (response != null) {
                logger.info("Prediction response for idInvestigador {}: {}", idInvestigador, response);
            } else {
                logger.warn("Prediction response is null for idInvestigador {}", idInvestigador);
            }

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("Error processing prediction for idInvestigador {}: {}", idInvestigador, e.getMessage());
            return ResponseEntity.notFound().build(); // Retornar 404 si ocurre un error
        }
    }
}
