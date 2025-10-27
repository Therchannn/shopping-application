package app.com.shoppingapp.controller;

import app.com.shoppingapp.dto.*;
import app.com.shoppingapp.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public String addToCart(@RequestBody ProductAddToCart data){
        return cartService.add(data);
    }

    @DeleteMapping("delete")
    public String deleteFromCart(@RequestBody ProductDeleteFromCart data){
        return cartService.delete(data);
    }

    @PostMapping("order")
    public String cartToOrder(@RequestBody CartToOrder data){
        return cartService.order(data);
    }

    @PatchMapping("/update")
    public String updateCart(@RequestBody CartToUpdate data){
        return cartService.update(data);
    }
}
