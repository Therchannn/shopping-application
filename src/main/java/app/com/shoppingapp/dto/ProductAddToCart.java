package app.com.shoppingapp.dto;

import lombok.Data;

@Data
public class ProductAddToCart {
    String userId;
    String productVariantId;
    int quantity;
}
