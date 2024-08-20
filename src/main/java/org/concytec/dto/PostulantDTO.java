package org.concytec.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class PostulantDTO {

    private BigInteger id;
    private BigInteger researchId;

    public static PostulantDTO create(BigInteger id, BigInteger researchId){
        return new PostulantDTO(id, researchId);
    }
}
