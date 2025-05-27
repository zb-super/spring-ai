package com.bz.order;

import com.bz.order.entity.OrderEntity;
import com.bz.order.entity.OrderItemEntity;
import com.bz.order.service.OrderItemService;
import com.bz.order.service.OrderService;
import org.apache.shardingsphere.infra.hint.HintManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Description
 * Copyright © 启明星辰 版权所有
 *
 * @author：binzhang
 * @date： 2025/5/24
 */
@SpringBootTest
public class DemoApplicationTests {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @Test
    void createOrder() {
        List<String> buyUserList = List.of("张三", "李四", "王五", "老七", "老六", "老四");
        List<String> sellerUserList = List.of("商家1", "商家2", "商家3", "商家4", "商家5", "商家6");

        for (int i = 0; i < 1; i++){
            String buyUser = "1";
            String sellerUser = "1";
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
            HintManager hintManager = HintManager.getInstance();

            orderService.insert(order);
//            orderItemService.insert(orderItemEntity);
        }
    }
}
