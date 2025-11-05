package app.com.shoppingapp.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantDTO {
    private String id_product;
    private String id_product_variant;
    private String code_product_variant;
    private String color;
    private String size;
    private int quantity;
    private BigDecimal price;
    private String imageUrl;
}