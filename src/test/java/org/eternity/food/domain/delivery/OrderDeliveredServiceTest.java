package org.eternity.food.domain.delivery;

import org.eternity.food.domain.generic.money.Money;
import org.eternity.food.domain.generic.money.Ratio;
import org.eternity.food.domain.order.Order;
import org.eternity.food.domain.order.OrderDeliveredService;
import org.eternity.food.domain.order.OrderRepository;
import org.eternity.food.domain.shop.Shop;
import org.eternity.food.domain.shop.ShopRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static java.util.Arrays.asList;
import static org.eternity.food.domain.Fixtures.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderDeliveredServiceTest {
    @Mock private OrderRepository orderRepository;
    @Mock private ShopRepository shopRepository;
    @Mock private DeliveryRepository deliveryRepository;

    private OrderDeliveredService orderDeliveredService;

    @Before
    public void setUp() {
        orderDeliveredService = new OrderDeliveredServiceImpl(orderRepository, shopRepository, deliveryRepository);
    }

    @Test
    public void 주문완료() {
        Shop shop = aShop()
                        .commissionRate(Ratio.valueOf(0.01))
                        .commission(Money.wons(1000))
                        .build();
        Order order = anOrder().items(asList(
                anOrderLineItem().groups(asList(
                        anOrderOptionGroup().options(asList(
                                anOrderOption().price(Money.wons(10000)).build())).build()))
                        .build()))
                .build();

        Delivery delivery = aDelivery().build();

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(shopRepository.findById(shop.getId())).thenReturn(Optional.of(shop));
        when(deliveryRepository.findById(delivery.getId())).thenReturn(Optional.of(delivery));

        orderDeliveredService.deliverOrder(order.getId());

        assertThat(order.getOrderStatus(), is(Order.OrderStatus.DELIVERED));
        assertThat(delivery.getDeliveryStatus(), is(Delivery.DeliveryStatus.DELIVERED));
        assertThat(shop.getCommission(), is(Money.wons(1100)));
    }
}
