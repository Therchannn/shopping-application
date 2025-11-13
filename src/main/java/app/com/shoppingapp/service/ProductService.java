package app.com.shoppingapp.service;

import app.com.shoppingapp.dto.CartDTO;
import app.com.shoppingapp.dto.CartToGet;
import app.com.shoppingapp.dto.ProductDTO;
import app.com.shoppingapp.dto.ProductVariantDTO;
import app.com.shoppingapp.entity.Product;
import app.com.shoppingapp.entity.ProductVariant;
import app.com.shoppingapp.mapper.ProductMapper;
import app.com.shoppingapp.repository.CartRepository;
import app.com.shoppingapp.repository.ProductRepository;
import app.com.shoppingapp.repository.ProductVariantsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductVariantsRepository productVariantsRepository;
    private final CartRepository cartRepository;

    public List<ProductDTO> get() {
        return productRepository.findAll().stream()
                .map(ProductMapper::toDTO)
                .collect(Collectors.toList());
    }


    public List<ProductDTO> search(String name){
        List<Product> result = productRepository.findByNameContainingIgnoreCase(name);
        return result.stream()
                .map(ProductMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO getById(String id){
        Optional<Product> result = productRepository.findById(id);
        if(result.isPresent()){
            ProductDTO product = ProductMapper.toDTO(result.get());
            Map<String, List<ProductVariantDTO>> groupVariant = product.getVariants().stream()
                    .collect(Collectors.groupingBy(ProductVariantDTO::getColor));
            product.setGroupedVariants(groupVariant);
            return product;
        }
        else{
            return null;
        }
    }

    public String addVariant(List<ProductVariantDTO> data){
        try {
            for(ProductVariantDTO variant : data){
                Optional<Product> result = productRepository.findById(variant.getId_product());

                if(result.isEmpty()){
                    return "Something is missing";
                }

                Product product = result.get();

                ProductVariant newVariant = ProductVariant.builder()
                        .idProductVariant(variant.getId_product_variant())
                        .codeProductVariant(variant.getId_product_variant())
                        .color(variant.getColor())
                        .price(variant.getPrice())
                        .quantity(variant.getQuantity())
                        .size(variant.getSize())
                        .imageUrl("/images/" + variant.getImageUrl())
                        .product(product)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();

                productVariantsRepository.save(newVariant);
            }
            return "Variant has been added";
        }catch (Exception e){
            return "Something is wrong" + e.getMessage();
        }
    }

    public String addProduct(ProductDTO data){
        Product newProduct = Product.builder()
                .id(data.getId())
                .name(data.getName())
                .description(data.getDescription())
                .status(data.getStatus())
                .category(data.getCategory())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        productRepository.save(newProduct);

        return "Thêm sản phẩm thành công!";
    }

    public String updateProduct(ProductDTO data) {
        Optional<Product> optionalProduct = productRepository.findById(data.getId());

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            product.setName(data.getName());
            product.setDescription(data.getDescription());
            product.setCategory(data.getCategory());
            product.setStatus(data.getStatus());

            productRepository.save(product);

            return "Cập nhật sản phẩm thành công";
        } else {
            return "Không tìm thấy sản phẩm với ID: " + data.getId();
        }
    }


    public long count(){
        return productRepository.count();
    }

    public List<Object[]> countByCategory(){
        return productRepository.countByCategory();
    }

    public Map<String, Long> getCategoryCounts() {
        List<Object[]> counts = productRepository.countByCategory();
        return counts.stream()
                .collect(Collectors.toMap(
                        arr -> (String) arr[0],  // category name
                        arr -> (Long) arr[1]     // count
                ));
    }
}
