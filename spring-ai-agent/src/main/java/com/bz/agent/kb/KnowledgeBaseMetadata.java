package com.bz.agent.kb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KnowledgeBaseMetadata {

    private Long id;

    private String name;

    private String description;
}
