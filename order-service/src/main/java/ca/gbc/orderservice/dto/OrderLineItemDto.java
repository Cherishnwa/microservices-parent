package ca.gbc.orderservice.dto;

import java.math.BigDecimal;

public record OrderLineItemDto(
        Long id,
        String skuCode,
        BigDecimal price,
        Integer quantity
) {}