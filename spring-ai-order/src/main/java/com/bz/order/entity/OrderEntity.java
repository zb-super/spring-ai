package com.bz.order.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description
 * Copyright © bz 版权所有
 *
 * @author：binzhang
 * @date： 2025/5/24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_order")
public class OrderEntity {

    @TableId
    private Long orderId;

    private String buyUser;

    private String sellerUser;

    private Long amount;

    private String createTime;

    private String status;

    private String marker;

}
