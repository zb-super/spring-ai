package com.bz.agent.model.agent;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AgentOptions {

    private String model;

    private String baseUrl;

    private String apiKey;

    private int maxToken;

    private Double temperature;

}
