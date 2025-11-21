package ca.gbc.orderservice.dto;

import java.util.List;

public record OrderRequest(
        List<OrderLineItemDto> orderLineItemDtoList
) {}