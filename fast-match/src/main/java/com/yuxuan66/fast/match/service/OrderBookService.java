package com.yuxuan66.fast.match.service;

import com.yuxuan66.fast.match.entity.dto.Order;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 订单薄
 *
 * @author Sir丶雨轩
 * @since 2023/9/13
 */
@Component
public class OrderBookService {

    /**
     * 订单薄 key = 股票类型，value = 订单
     */
    private final Map<String, List<Order>> orderBook = new ConcurrentHashMap<>();

    /**
     * 添加订单到订单薄
     *
     * @param order 订单
     */
    public void addOrder(Order order) {
        List<Order> orderList;
        if (orderBook.containsKey(order.getKey())) {
            orderList = orderBook.get(order.getKey());
        } else {
            orderList = new LinkedList<>();
        }
        orderList.add(order);
        orderBook.put(order.getKey(), orderList);
    }

    /**
     * 获取订单薄数据
     *
     * @param key 股票类型
     * @return 订单薄数据
     */
    public List<Order> getOrders(String key) {
        if (orderBook.containsKey(key)) {
            return orderBook.get(key);
        }
        return new LinkedList<>();
    }

    /**
     * 按照价格排序获取前size个订单
     *
     * @param key   股票类型
     * @param isBuy 是否是买单
     * @param size  获取订单数量
     * @return 订单列表
     */
    public List<Order> getOrders(String key, boolean isBuy, int size) {
        // 获取订单，并过滤指定类型订单
        List<Order> orders = getOrders(key).stream().filter(item -> item.isBuy() == isBuy).sorted(Comparator.comparingLong(Order::getPrice)).toList();
        if (orders.isEmpty()) {
            return orders;
        }
        if (orders.size() > size) {
            return orders.subList(0, size);
        }
        return orders;
    }

}
