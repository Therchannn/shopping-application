package app.com.shoppingapp.service;

import app.com.shoppingapp.dto.ProductDTO;
import app.com.shoppingapp.entity.Product;
import app.com.shoppingapp.mapper.ProductMapper;
import app.com.shoppingapp.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

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


    public long count(){
        return productRepository.count();
    }

    public List<Object[]> countByCategory(){
        return productRepository.countByCategory();
    }

//    public String add(ProductAddToCart data, String user_id){
//        try{
//            cartRepository.save();
//        }
//        catch (Exception e) {
//        }
//    }
}
