package com.shoes.webshoes.response;

import lombok.Data;
import java.util.List;

@Data
public class GHNServiceListResponse {
    private Integer code;
    private String code_message_value;
    private List<GHNServiceResponse> data;
    private String message;
} 