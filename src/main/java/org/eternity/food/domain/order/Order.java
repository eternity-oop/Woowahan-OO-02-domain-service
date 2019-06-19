package org.eternity.food.domain.order;

import lombok.Builder;
import lombok.Getter;
import org.eternity.food.domain.generic.money.Money;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Entity
@Table(name="ORDERS")
@Getter
public class Order {
    public enum OrderStatus { ORDERED, PAYED, DELIVERED }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ORDER_ID")
    private Long id;

    @Column(name="USER_ID")
    private Long userId;

    @Column(name="SHOP_ID")
    private Long shopId;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="ORDER_ID")
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    @Column(name="ORDERED_TIME")
    private LocalDateTime orderedTime;

    @Enumerated(EnumType.STRING)
    @Column(name="STATUS")
    private OrderStatus orderStatus;

    public Order(Long userId, Long shopId, List<OrderLineItem> items) {
        this(null, userId, shopId, items, LocalDateTime.now(), null);
    }

    @Builder
    public Order(Long id, Long userId, Long shopId, List<OrderLineItem> items, LocalDateTime orderedTime, OrderStatus status) {
        this.id = id;
        this.userId = userId;
        this.shopId = shopId;
        this.orderedTime = orderedTime;
        this.orderStatus = status;
        this.orderLineItems.addAll(items);
    }

    Order() {
    }

    public List<Long> getMenuIds() {
        return orderLineItems.stream().map(OrderLineItem::getMenuId).collect(toList());
    }

    public void place(OrderValidator orderValidator) {
        orderValidator.validate(this);
        ordered();
    }

    private void ordered() {
        this.orderStatus = OrderStatus.ORDERED;
    }

    public void payed() {
        this.orderStatus = OrderStatus.PAYED;
    }

    public void delivered() {
        this.orderStatus = OrderStatus.DELIVERED;
    }

    public Money calculateTotalPrice() {
        return Money.sum(orderLineItems, OrderLineItem::calculatePrice);
    }
}
