package app.com.shoppingapp.dto;

import lombok.Data;

@Data
public class ProductAddToCart {
    String userId;
    String productId;
    String productVariantId;
    int quantity;
}
