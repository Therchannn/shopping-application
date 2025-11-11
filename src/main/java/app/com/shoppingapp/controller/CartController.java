package app.com.shoppingapp.controller;

import app.com.shoppingapp.dto.*;
import app.com.shoppingapp.service.CartService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/cart")
public class CartController {
    private final CartService cartService;

    @GetMapping("get")
    public List<CartToGet> getCart(@RequestParam("id") String data){
        return cartService.get(data);
    }

    @PostMapping("add")
    public String addToCart(HttpSession session, @RequestBody ProductAddToCart data){
        String id = (String) session.getAttribute("id");
        if(id == null){
            return "Vui lòng đăng nhập tài khoản";
        }
        else{
            data.setUserId(id);
            return cartService.add(data);
        }
    }

    @DeleteMapping("delete")
    public String deleteFromCart(@RequestBody Map<String, String> body, HttpSession session){
        String userId = (String) session.getAttribute("id");
        if(userId == null){
            return "Something is missing";
        }
        String productVariantId = body.get("id");
        return cartService.delete(productVariantId, userId);
    }

    @PostMapping("order")
    public String cartToOrder(@RequestBody Map<String, String> body, HttpSession session){
        String userId = (String) session.getAttribute("id");
        String payment = body.get("payment");
        return cartService.order(userId, payment);
    }

    @PostMapping("/update")
    public String updateCart(@RequestBody Map<String, String> body, HttpSession session){
        String userId = (String) session.getAttribute("id");
        if(userId == null){
            return "Something is missing";
        }

        CartToUpdate data = new CartToUpdate();
        data.setUserId(userId);
        data.setProductVariantId(body.get("productVariantId"));
        data.setQuantity(Integer.parseInt(body.get("quantity")));

        return cartService.update(data);
    }
}
