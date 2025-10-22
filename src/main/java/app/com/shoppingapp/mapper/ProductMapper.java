package app.com.shoppingapp.mapper;

import app.com.shoppingapp.dto.*;
import app.com.shoppingapp.entity.*;
import java.util.List;
import java.util.stream.Collectors;

public class ProductMapper {

    public static ProductDTO toDTO(Product product) {
        if (product == null) return null;

        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .category(product.getCategory())
                .status(product.getStatus())
                .variants(toVariantDTOList(product.getVariants()))
                .build();
    }

     private static List<ProductVariantDTO> toVariantDTOList(List<ProductVariant> variants) {
        if (variants == null) return null;
        return variants.stream()
                .map(ProductMapper::toVariantDTO)
                .collect(Collectors.toList());
    }

     public static ProductVariantDTO toVariantDTO(ProductVariant variant) {
        return ProductVariantDTO.builder()
                .id_product_variant(variant.getIdProductVariant())
                .code_product_variant(variant.getCodeProductVariant())
                .color(variant.getColor())
                .size(variant.getSize())
                .quantity(variant.getQuantity())
                .price(variant.getPrice())
                .imageUrl(variant.getImageUrl())
                .build();
    }
}
