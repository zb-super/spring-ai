package com.bz.agent.kb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description
 * Copyright © 启明星辰 版权所有
 *
 * @author：binzhang
 * @date： 2025/5/19
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KnowledgeBaseOptions {

    private Integer topk;

    private Integer topN;

    private String rerank;
}
