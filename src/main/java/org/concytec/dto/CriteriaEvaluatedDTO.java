    package org.concytec.dto;

    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import java.math.BigDecimal;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor

    public class CriteriaEvaluatedDTO {
        private String name;
        private BigDecimal value;

        public static CriteriaEvaluatedDTO create(String name, BigDecimal value){
            return new CriteriaEvaluatedDTO(name, value);
        }
    }
