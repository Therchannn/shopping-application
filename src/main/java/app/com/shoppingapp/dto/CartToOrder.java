package app.com.shoppingapp.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartToOrder {
    @Data
    public static class productToOrder{
        String productVariantId;
        int quantity;
    }
    List<productToOrder> productVariants;
    String userId;
}

