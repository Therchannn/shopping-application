package app.com.shoppingapp.mapper;

import app.com.shoppingapp.dto.CartDTO;
import app.com.shoppingapp.entity.Cart;

public class CartMapper {

    public static CartDTO toDTO(Cart cart) {
        if (cart == null) return null;

        return CartDTO.builder()
                .quantity(cart.getQuantity())
                .createdAt(cart.getCreatedAt())
                .cartProduct(ProductMapper.toVariantDTO(cart.getProductVariant()))
                .build();
    }

}
