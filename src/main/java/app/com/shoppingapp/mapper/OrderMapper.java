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
                .paymentMethod(data.getPaymentMethod() != null ? data.getPaymentMethod().name() : null)
                .createdAt(data.getCreatedAt())
                .items(data.getItems())
                .userId(data.getUser() != null ? data.getUser().getId() : null)
                .userName(data.getUser() != null ? data.getUser().getName() : "N/A")
                .userUsername(data.getUser() != null ? data.getUser().getUsername() : "N/A")
                .userPhone(data.getUser() != null ? data.getUser().getPhone() : "N/A")
                .build();
    }
}
