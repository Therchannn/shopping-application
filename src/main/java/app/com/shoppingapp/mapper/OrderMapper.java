package app.com.shoppingapp.mapper;

import app.com.shoppingapp.dto.OrderDTO;
import app.com.shoppingapp.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    public static OrderDTO toDTO(Order data){
        return OrderDTO.builder()
                .id(data.getId())
                .total(data.getTotal())
                .shipping_fee(data.getShippingFee())
                .status(data.getStatus())
                .paymentMethod(data.getPaymentMethod())
                .createdAt(data.getCreatedAt())
                .items(data.getItems())
                .build();
    }
}
