package app.com.shoppingapp.mapper;

import app.com.shoppingapp.dto.OrderDTO;
import app.com.shoppingapp.dto.OrderItemDTO;
import app.com.shoppingapp.dto.OrderToGet;
import app.com.shoppingapp.entity.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public static List<OrderDTO> groupOrders(List<OrderToGet> dataList) {
        Map<String, List<OrderToGet>> grouped = dataList.stream()
                .collect(Collectors.groupingBy(OrderToGet::getId));

        return grouped.entrySet().stream()
                .map(entry -> {
                    String orderId = entry.getKey();
                    List<OrderToGet> items = entry.getValue();

                    OrderToGet first = items.get(0);

                    List<OrderItemDTO> itemDTOs = items.stream()
                            .map(i -> OrderItemDTO.builder()
                                    .nameProduct(i.getNameProduct())
                                    .idVariant(i.getIdVariant())
                                    .color(i.getColor())
                                    .quantity(i.getQuantity())
                                    .size(i.getSize())
                                    .price(i.getPrice())
                                    .image(i.getImage())
                                    .build())
                            .toList();

                    return OrderDTO.builder()
                            .id(orderId)
                            .total(first.getTotal())
                            .status(first.getStatus())
                            .shipping_fee(first.getShippingFee())
                            .paymentMethod(Order.PaymentMethod.valueOf(first.getMethodPayment()))
                            .createdAt(first.getCreatedAt().toLocalDateTime())
                            .items(itemDTOs)
                            .build();
                })
                .toList();
    }
}
