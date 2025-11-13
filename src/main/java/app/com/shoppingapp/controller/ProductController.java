package app.com.shoppingapp.controller;

import app.com.shoppingapp.dto.ProductDTO;
import app.com.shoppingapp.service.ProductService;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/get")
    public List<ProductDTO> getProducts(){
        return productService.get();
    }


    @GetMapping("/{id}")
    public ProductDTO getDetailById(@PathVariable("id") String id){
        return productService.getById(id);
    }

    @GetMapping()
    public List<ProductDTO> searchProduct(@RequestParam("search") String name){
        return productService.search(name);
    }
}
