package app.com.shoppingapp.controller;

import app.com.shoppingapp.dto.OrderDTO;
import app.com.shoppingapp.dto.OrderToChange;
import app.com.shoppingapp.dto.OrderToGet;
import app.com.shoppingapp.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;

    @GetMapping("get")
    public List<OrderDTO> getOrder(@Param("id") String id){
        return orderService.get(id);
    }

//    @PatchMapping("/change")
//    public String deleteOrder(@RequestBody OrderToChange data){
//        return orderService.change(data);
//    }

}
