package app.com.shoppingapp.controller;

import app.com.shoppingapp.dto.CartDTO;
import app.com.shoppingapp.dto.ProductDTO;
import app.com.shoppingapp.dto.UserDTO;
import app.com.shoppingapp.dto.UserToSignUp;
import app.com.shoppingapp.service.CartService;
import app.com.shoppingapp.service.ProductService;
import app.com.shoppingapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class PageController {
    private final ProductService productService;
    private final UserService userService;
    private final CartService cartService;

    @GetMapping("/home")
    public String homePage(Model model){
        List<ProductDTO> products = productService.get();

        model.addAttribute("products", products);

        return "home";
    }

    @GetMapping("/signIn")
    public String signInPage() {
        return "signIn"; 
    }

    // Hiển thị form đăng ký

    @GetMapping("/signUp")
    public String showSignUpForm(Model model) {
        model.addAttribute("user", new UserToSignUp());
        return "signUp";
    }
    @GetMapping("/contact")
    public String contactPage() {
        return "contact"; 
    }
    @GetMapping("/cart")
    public String cartPage(Model model) {
        // List<CartDTO> carts = cartService.get();

        // model.addAttribute("carts", carts);

        return "cart";
    }
    //  @GetMapping("/cart")
    // public String cartPage(@RequestParam("id") String userId, Model model) {
    //      List<CartDTO> cartItems = cartService.get(userId);

    //     model.addAttribute("cartItems", cartItems);

    //     return "cart";
    // }
}

