package app.com.shoppingapp.controller;

import java.util.Arrays;
import java.util.List;

import app.com.shoppingapp.dto.ProductVariantDTO;
import app.com.shoppingapp.dto.UserDTO;
import app.com.shoppingapp.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import app.com.shoppingapp.dto.ProductDTO;
import app.com.shoppingapp.dto.UserToSignIn;
import app.com.shoppingapp.service.AuthService;
import app.com.shoppingapp.service.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class PageController {

    private static final String AUTH_SESSION_KEY = "isAdmin";
    private static final String ADMIN_LOGIN_URL = "redirect:/admin/login";
    private static final String ADMIN_DASHBOARD_URL = "redirect:/admin/dashboard";

    private final ProductService productService;
    private final AuthService authService;
    private final UserService userService;

    @GetMapping("/home")
    public String homePage(Model model){
        List<ProductDTO> allProducts = productService.get();
        List<ProductDTO> products = allProducts.subList(0, Math.min(8, allProducts.size()));
        List<ProductDTO> newProducts = allProducts.subList(Math.min(allProducts.size(), allProducts.size() - 8),  allProducts.size());

        model.addAttribute("products", products);
        model.addAttribute("newProducts", newProducts);

        return "home";
    }

    @GetMapping("/layout")
    public String layoutPage(){
        return "layout";
    }

    @GetMapping("/admin")
    public String adminRoot() {
        return ADMIN_LOGIN_URL;
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
            session.setAttribute(AUTH_SESSION_KEY, true);
            return ADMIN_DASHBOARD_URL;
        } else {
            session.removeAttribute(AUTH_SESSION_KEY);
            model.addAttribute("loginError", "Wrong username or password");
            return "admin/login";
        }
    }


    @GetMapping("/admin/product")
    public String adminProduct(Model model){
        List<ProductDTO> products = productService.get();
        model.addAttribute("products", products);

        return "admin/product";
    }

    @PostMapping("/admin/variant/add")
    public String addVariant(@RequestParam("variantsJson") String variantsJson) {
        ObjectMapper mapper = new ObjectMapper();
        List<ProductVariantDTO> variants;
        try {
            variants = mapper.readValue(variantsJson, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        productService.addVariant(variants);
        return "redirect:/admin/product";
    }


    @GetMapping("/admin/logout")
    public String adminLogout(HttpSession session) {
        session.invalidate();
        return ADMIN_LOGIN_URL;
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
    public String infoPage(Model model){
        UserDTO info = userService.getInfo("U002");
        model.addAttribute("info", info);
        return "info";
    }

    @GetMapping("/logout")
    public String logoutPage(){
        return "logout";
    }

    @PostMapping("/update")
    public String infoPage(@ModelAttribute("user") UserDTO updateInfo){
        updateInfo.setId("U002");
        userService.update(updateInfo);

        return "redirect:/info";
    }

    @GetMapping("/product")
    public String productPage(Model model){
        List<ProductDTO> products = productService.get();

        model.addAttribute("products", products);

        return "product";
    }

    @GetMapping("/product/{id}")
    public String productDetailsPage(Model model, @PathVariable("id") String id){
        ProductDTO product = productService.getById(id);

        model.addAttribute("product", product);

        return "productDetails";
    }

}
