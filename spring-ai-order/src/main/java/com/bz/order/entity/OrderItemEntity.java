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
@TableName("order_item")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemEntity {

    @TableId
    private Long id;

    private Long orderId;

    private String itemName;

    private Long amount;

    private Long num;

}
