package app.com.shoppingapp.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import app.com.shoppingapp.dto.ProductDTO;
import app.com.shoppingapp.dto.UserToSignIn;
import app.com.shoppingapp.service.AuthService;
import app.com.shoppingapp.service.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class PageController {

    private final ProductService productService;
    private final AuthService authService;

    @GetMapping("/home")
    public String homePage(Model model){
        List<ProductDTO> products = productService.get();

        model.addAttribute("products", products);

        return "home";
    }

    @GetMapping("/layout")
    public String layoutPage(){
        return "layout";
    }

    @GetMapping("/admin")
    public String adminRoot() {
        return "redirect:/admin/login";
    }

    @GetMapping("/admin/login")
    public String adminLoginPage() {
        return "admin/login";
    }

    @PostMapping("/admin/login")
    public String adminLoginForm(@RequestParam String username,
                                 @RequestParam String password,
                                 HttpSession session,
                                 Model model) {
        UserToSignIn req = new UserToSignIn();
        req.setUsername(username);
        req.setPassword(password);

        if (authService.authenticateAdmin(req)) {
            session.setAttribute("isAdmin", true);
            return "redirect:/admin/home";
        } else {
            session.removeAttribute("isAdmin");
            model.addAttribute("loginError", "Wrong username or password");
            return "admin/login";
        }
    }

    @GetMapping("/admin/home")
    public String adminHome(Model model, HttpSession session) {
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        if (isAdmin == null || !isAdmin) {
            return "redirect:/admin/login";
        }
        List<ProductDTO> products = productService.get();
        model.addAttribute("products", products);
        return "admin/home";
    }

    @GetMapping("/admin/logout")
    public String adminLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }

        @GetMapping("/about")
    public String aboutPage(){
        return "about";
    }

    @GetMapping("/layoutInfo")
    public String layoutInfoPage(){
        return "layoutInfo";
    }

    @GetMapping("/info")
    public String infoPage(){
        return "info";
    }

    @GetMapping("/product")
    public String productPage(Model model){
        List<ProductDTO> products = productService.get();

        model.addAttribute("products", products);

        return "product";
    }

    @GetMapping("/productDetails")
    public String productDetailsPage(Model model){
        List<ProductDTO> products = productService.get();

        model.addAttribute("products", products);

        return "productDetails";
    }

}
