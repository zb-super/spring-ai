package com.bz.agent.model.kb;

/**
 * Description
 * Copyright © 启明星辰 版权所有
 *
 * @author：binzhang
 * @date： 2025/5/19
 */
public interface KnowledgeBaseCallBack {

    public KnowledgeBaseMetadata getMetadata();

    public String call(Long id, String searchMsg, KnowledgeBaseOptions knowledgeBaseOptions);

}
