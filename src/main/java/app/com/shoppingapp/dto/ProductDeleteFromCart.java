package app.com.shoppingapp.dto;

import lombok.Data;

@Data
public class ProductDeleteFromCart {
    String productVariantId;
    String userId;
}
