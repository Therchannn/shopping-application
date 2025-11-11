package app.com.shoppingapp.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderItemDTO {
    private String nameProduct;
    private String idVariant;
    private String color;
    private Integer quantity;
    private String size;
    private BigDecimal price;
    private String image;
}