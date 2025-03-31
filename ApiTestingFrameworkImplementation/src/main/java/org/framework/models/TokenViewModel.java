package org.framework.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenViewModel {
    private String token;
    private String expires;
    private String status;
    private  String result;
}
