package org.concytec.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.concytec.exception.GenericClientException;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ResearcherAttribute {
    INDEX_H(0, "Indice H"),
    DEPARTMENT(1, "Departamento"),
    AGE_RESEARCHER(2, "Edad del investigador"),
    COUNT_POST(3, "Cantidad de publicaciones"),
    PROBABILITY(4, "Índice de probabilidad"),
    POSTS(5, "Índice Publicaciones"),
    ACADEMIC_DEGREE(6, "Grado académico");

    private final int code;
    private final String description;

    public static ResearcherAttribute fromCode(int code) {
        return Arrays.stream(ResearcherAttribute.values())
                .filter(attribute -> attribute.getCode() == code)
                .findFirst()
                .orElseThrow(() -> new GenericClientException("NOT_FOUND","Invalid code: " + code, HttpStatus.NOT_FOUND));
    }
}

