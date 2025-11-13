package app.com.shoppingapp.controller;

import app.com.shoppingapp.dto.OrderDTO;
import app.com.shoppingapp.dto.OrderToChange;
import app.com.shoppingapp.dto.OrderToGet;
import app.com.shoppingapp.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;

    @GetMapping("get")
    public List<OrderDTO> getOrder(@Param("id") String id){
        return orderService.getById(id);
    }

    @PostMapping("update")
    public void updateOrder(@RequestBody Map<String,String> data){
        orderService.update(data.get("orderId"), data.get("status"), data.get("payment"));
    }
}
