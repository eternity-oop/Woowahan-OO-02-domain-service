package org.eternity.food.domain.order;

import org.junit.Test;

import static org.eternity.food.domain.Fixtures.aShop;
import static org.eternity.food.domain.Fixtures.anOrder;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class OrderTest {
    @Test
    public void 결제완료() {
        Order order = anOrder().status(Order.OrderStatus.ORDERED).build();

        order.payed();

        assertThat(order.getOrderStatus(), is(Order.OrderStatus.PAYED));
    }

    @Test
    public void 배송완료() {
        Order order = anOrder()
                        .shopId(aShop().build().getId())
                        .status(Order.OrderStatus.PAYED)
                        .build();

        order.delivered();

        assertThat(order.getOrderStatus(), is(Order.OrderStatus.DELIVERED));
    }
}
