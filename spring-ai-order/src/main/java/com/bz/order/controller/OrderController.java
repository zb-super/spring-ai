package com.bz.order.controller;

import com.bz.order.entity.OrderEntity;
import com.bz.order.entity.OrderItemEntity;
import com.bz.order.service.OrderItemService;
import com.bz.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description
 * Copyright © bz 版权所有
 *
 * @author：binzhang
 * @date： 2025/5/24
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @RequestMapping("/create")
    @Transactional
    public Integer createOrder(){
        List<String> buyUserList = List.of("张三", "李四", "王五", "老七", "老六", "老四");
        List<String> sellerUserList = List.of("商家1", "商家2", "商家3", "商家4", "商家5", "商家6");

        for (int i = 0; i < 100; i++){
            String buyUser = buyUserList.get(i/buyUserList.size());
            String sellerUser = sellerUserList.get(i/ sellerUserList.size());
            Long orderId = System.currentTimeMillis();

            OrderEntity order = OrderEntity.builder()
                    .orderId(orderId)
                    .buyUser(buyUser)
                    .sellerUser(sellerUser)
                    .amount((long) i)
                    .createTime("" + System.currentTimeMillis())
                    .status("CREATE")
                    .marker("1")
                    .build();

            OrderItemEntity orderItemEntity = OrderItemEntity.builder()
                    .orderId(orderId)
                    .itemName("" + System.currentTimeMillis())
                    .amount(10l)
                    .num(1l)
                    .build();

            orderService.insert(order);
            orderItemService.insert(orderItemEntity);
        }
        return 1;
    }
}
