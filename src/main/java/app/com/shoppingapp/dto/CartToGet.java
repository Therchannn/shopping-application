package app.com.shoppingapp.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartToGet {
    private String id;
    private String name;
    private String description;
    private String category;
    private String code;
    private String size;
    private String color;
    private String imageUrl;
    private BigDecimal price;
    private Integer quantity;
}
