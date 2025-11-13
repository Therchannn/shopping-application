package app.com.shoppingapp.dto;

import app.com.shoppingapp.entity.Order;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderToGet {
    private String id;
    private BigDecimal total;
    private String status;
    private BigDecimal shippingFee;
    private String methodPayment;
    private String nameProduct;
    private String idVariant;
    private String color;
    private Integer quantity;
    private String size;
    private BigDecimal price;
    private String image;
    private Timestamp createdAt;
}
