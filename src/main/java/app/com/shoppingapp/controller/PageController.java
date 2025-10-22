package app.com.shoppingapp.controller;

import app.com.shoppingapp.dto.ProductDTO;
import app.com.shoppingapp.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class PageController {
    private final ProductService productService;

    @GetMapping("/home")
    public String homePage(Model model){
        List<ProductDTO> products = productService.get();

        model.addAttribute("products", products);

        return "home";
    }
}
