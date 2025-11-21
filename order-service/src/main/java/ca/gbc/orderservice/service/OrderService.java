package ca.gbc.orderservice.service;

import ca.gbc.orderservice.dto.OrderLineItemDto;
import ca.gbc.orderservice.dto.OrderRequest;
import ca.gbc.orderservice.model.Order;
import ca.gbc.orderservice.model.OrderLineItem;
import ca.gbc.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest) {
        // Generate unique order number
        String orderNumber = UUID.randomUUID().toString();

        // Convert DTOs to Entities
        List<OrderLineItem> orderLineItems = orderRequest.orderLineItemDtoList()
                .stream()
                .map(this::mapToOrderLineItem)
                .toList();

        // Create Order entity
        Order order = Order.builder()
                .orderNumber(orderNumber)
                .orderLineItems(orderLineItems)
                .build();

        // Save order (cascade will save line items)
        orderRepository.save(order);

        log.info("Order {} placed successfully with {} items",
                orderNumber, orderLineItems.size());
    }

    private OrderLineItem mapToOrderLineItem(OrderLineItemDto dto) {
        return OrderLineItem.builder()
                .skuCode(dto.skuCode())
                .price(dto.price())
                .quantity(dto.quantity())
                .build();
    }
}