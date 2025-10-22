package app.com.shoppingapp.dto;

import lombok.Data;

@Data
public class CartToUpdate {
    String userId;
    String productVariantId;
    int quantity;
}
