package app.com.shoppingapp.mapper;

import app.com.shoppingapp.dto.OrderDTO;
import app.com.shoppingapp.dto.OrderItemDTO;
import app.com.shoppingapp.dto.OrderToGet;
 import app.com.shoppingapp.dto.OrderItemDTO;
import app.com.shoppingapp.entity.Order;
import app.com.shoppingapp.entity.OrderItem;
import app.com.shoppingapp.entity.ProductVariant;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.stream.Collectors;

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
                                    .productName(i.getNameProduct())
                                    .variantId(i.getIdVariant())
                                    .color(i.getColor())
                                    .quantity(i.getQuantity())
                                    .size(i.getSize())
                                    .price(i.getPrice())
                                    .productImage(i.getImage())
                                    .build())
                            .toList();

                    return OrderDTO.builder()
                            .id(orderId)
                            .total(first.getTotal())
                            .status(first.getStatus())
                            .shippingFee(first.getShippingFee())
                            .paymentMethod(first.getMethodPayment())
                            .createdAt(first.getCreatedAt().toLocalDateTime())
                            .items(itemDTOs)
                            .build();
                })
                .toList();
    }

    public static OrderItemDTO toItemDTO(OrderItem item) {
        ProductVariant variant = item.getProductVariant();

        if (variant == null) {
            return OrderItemDTO.builder()
                    .productName("N/A")
                    .quantity(item.getQuantity())
                    .build();
        }

        String imageUrl = variant.getImageUrl();
        // Nếu variant không có image, lấy từ product
        if ((imageUrl == null || imageUrl.isEmpty())
            && variant.getProduct() != null
            && variant.getProduct().getVariants() != null
            && !variant.getProduct().getVariants().isEmpty()) {
            imageUrl = variant.getProduct().getVariants().get(0).getImageUrl();
        }

        return OrderItemDTO.builder()
                .productId(variant.getProduct() != null ? variant.getProduct().getId() : null)
                .productName(variant.getProduct() != null ? variant.getProduct().getName() : "N/A")
                .productImage(imageUrl)
                .variantId(variant.getIdProductVariant())
                .color(variant.getColor())
                .size(variant.getSize())
                .price(variant.getPrice())
                .quantity(item.getQuantity())
                .totalPrice(variant.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .build();
    }

    public static OrderDTO toDTO(Order data){
        return OrderDTO.builder()
                .id(data.getId())
                .total(data.getTotal())
                .shippingFee(data.getShippingFee())
                .status(data.getStatus())
                .paymentMethod(data.getPaymentMethod() != null ? data.getPaymentMethod().name() : null)
                .createdAt(data.getCreatedAt())
                .items(data.getItems() != null ?
                       data.getItems().stream()
                           .map(OrderMapper::toItemDTO)
                           .collect(Collectors.toList()) :
                       null)
                .userId(data.getUser() != null ? data.getUser().getId() : null)
                .userName(data.getUser() != null ? data.getUser().getName() : "N/A")
                .userUsername(data.getUser() != null ? data.getUser().getUsername() : "N/A")
                .userPhone(data.getUser() != null ? data.getUser().getPhone() : "N/A")
                .userAddress(data.getUser() != null ? data.getUser().getAddress() : "N/A")
                .build();
    }
}
