package org.concytec.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Converter
public class JsonBigDecimalListConverter implements AttributeConverter<List<BigDecimal>, String> {
    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<BigDecimal> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON writing error", e);
        }
    }

    @Override
    public List<BigDecimal> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, objectMapper.getTypeFactory().constructCollectionType(List.class, BigDecimal.class));
        } catch (IOException e) {
            throw new RuntimeException("JSON reading error", e);
        }
    }
}