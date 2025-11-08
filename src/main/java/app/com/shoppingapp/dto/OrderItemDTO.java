package app.com.shoppingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDTO {
    private String productId;
    private String productName;
    private String productImage;
    private String variantId;
    private String color;
    private String size;
    private BigDecimal price;
    private int quantity;
    private BigDecimal totalPrice;
}

