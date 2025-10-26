package app.com.shoppingapp.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import app.com.shoppingapp.dto.ProductDTO;
import app.com.shoppingapp.service.ProductService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class PageController {
    private final ProductService productService;

    @GetMapping("/home")
    public String homePage(Model model){
        // List<ProductDTO> products = productService.get();

        // model.addAttribute("products", products);

        return "home";
    }

    @GetMapping("/layout")
    public String layoutPage(){
        return "layout";
    }

    @GetMapping("/layoutProduct")
    public String layoutProductPage(Model model){

        List<ProductDTO> products = productService.get();

        model.addAttribute("products", products);

        return "layoutProduct";
    }

    
}
