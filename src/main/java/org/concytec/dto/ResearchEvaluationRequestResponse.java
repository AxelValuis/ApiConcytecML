package org.concytec.dto;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Data
public class ResearchEvaluationRequestResponse {

    @JsonProperty("Importancia_variables")
    private List<Double> importanciaVariables;

    @JsonProperty("Probabilidades")
    private double probabilidades;
}
