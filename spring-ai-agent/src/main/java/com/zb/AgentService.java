package com.zb;

import com.zb.event.MsgEventHandle;

/**
 * Description
 * Copyright © 启明星辰 版权所有
 *
 * @author：binzhang
 * @date： 2025/5/12
 */
public interface AgentService {

    public void startTask(Long botId, String query, MsgEventHandle msgEventHandle);

}
