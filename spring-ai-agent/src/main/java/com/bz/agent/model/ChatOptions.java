package com.bz.agent.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatOptions {

    private String model;

    private String baseUrl;

    private String apiKey;

    private int maxToken;

    private Double temperature;

}
