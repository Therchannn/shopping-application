package app.com.shoppingapp.mapper;

import app.com.shoppingapp.dto.*;
import app.com.shoppingapp.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;

public class ProductMapper {

    public static ProductDTO toDTO(Product product) {
        if (product == null) return null;

        List<ProductVariantDTO> variantDTOs = toVariantDTOList(product.getVariants());

        Map<String, List<ProductVariantDTO>> groupedByColor = new LinkedHashMap<>();

        for (ProductVariantDTO v : variantDTOs) {
            groupedByColor
                    .computeIfAbsent(v.getColor(), k -> new ArrayList<>())
                    .add(v);
        }

        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .category(product.getCategory())
                .status(product.getStatus())
                .variants(variantDTOs)
                .groupedVariants(groupedByColor)
                .build();
    }

     private static List<ProductVariantDTO> toVariantDTOList(List<ProductVariant> variants) {
        if (variants == null) return null;
        return variants.stream()
                .map(ProductMapper::toVariantDTO)
                .collect(Collectors.toList());
    }

     public static ProductVariantDTO toVariantDTO(ProductVariant variant)
     {
        return ProductVariantDTO.builder()
                .id_product(variant.getProduct().getId())
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
