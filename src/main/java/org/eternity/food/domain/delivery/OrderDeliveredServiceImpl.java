package org.eternity.food.domain.delivery;

import org.eternity.food.domain.order.Order;
import org.eternity.food.domain.order.OrderDeliveredService;
import org.eternity.food.domain.order.OrderPayedService;
import org.eternity.food.domain.order.OrderRepository;
import org.eternity.food.domain.shop.Shop;
import org.eternity.food.domain.shop.ShopRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderDeliveredServiceImpl implements OrderPayedService, OrderDeliveredService {
    private OrderRepository orderRepository;
    private ShopRepository shopRepository;
    private DeliveryRepository deliveryRepository;

    public OrderDeliveredServiceImpl(OrderRepository orderRepository,
                                     ShopRepository shopRepository,
                                     DeliveryRepository deliveryRepository) {
        this.orderRepository = orderRepository;
        this.shopRepository = shopRepository;
        this.deliveryRepository = deliveryRepository;
    }

    @Override
    public void payOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(IllegalArgumentException::new);
        order.payed();

        Delivery delivery = Delivery.started(orderId);
        deliveryRepository.save(delivery);
    }

    @Override
    public void deliverOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(IllegalArgumentException::new);
        Shop shop = shopRepository.findById(order.getShopId()).orElseThrow(IllegalArgumentException::new);
        Delivery delivery = deliveryRepository.findById(orderId).orElseThrow(IllegalArgumentException::new);

        order.delivered();
        shop.billCommissionFee(order.calculateTotalPrice());
        delivery.complete();
    }
}
