package app.com.shoppingapp.dto;

import app.com.shoppingapp.entity.Order;
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
    Order.PaymentMethod paymentMethod;
    String userId;
}
