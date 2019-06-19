package org.eternity.food.service.order;

import org.eternity.food.domain.order.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private OrderRepository orderRepository;
    private OrderValidator orderValidator;
    private OrderMapper orderMapper;
    private OrderDeliveredService orderDeliveredService;
    private OrderPayedService orderPayedService;

    public OrderService(OrderMapper orderMapper,
                        OrderRepository orderRepository,
                        OrderValidator orderValidator,
                        OrderDeliveredService orderDeliveredService,
                        OrderPayedService orderPayedService) {
        this.orderMapper = orderMapper;
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
        this.orderDeliveredService = orderDeliveredService;
        this.orderPayedService = orderPayedService;
    }

    @Transactional
    public void placeOrder(Cart cart) {
        Order order = orderMapper.mapFrom(cart);
        order.place(orderValidator);
        orderRepository.save(order);
    }

    @Transactional
    public void payOrder(Long orderId) {
        orderPayedService.payOrder(orderId);
    }

    @Transactional
    public void deliverOrder(Long orderId) {
        orderDeliveredService.deliverOrder(orderId);
    }
}
