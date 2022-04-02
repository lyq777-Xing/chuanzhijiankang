package net.zjitc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sun.org.apache.xpath.internal.operations.Or;
import net.zjitc.entity.Order;
import net.zjitc.entity.OrderSuccess;

import java.util.List;
import java.util.Map;

public interface OrderService extends IService<Order> {
    List<Order> findByMemberId(Integer memberId);
    Order addOrder(Order order);
    OrderSuccess findById(Integer orderId);
}
