package app.com.shoppingapp.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CartId implements Serializable {
    private String productVariantId;
    private String userId;
}
