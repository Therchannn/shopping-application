package app.com.shoppingapp.service;

import app.com.shoppingapp.dto.OrderToChange;
import app.com.shoppingapp.entity.Order;
import app.com.shoppingapp.mapper.OrderMapper;
import app.com.shoppingapp.repository.OrderRepository;
import app.com.shoppingapp.dto.OrderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public List<OrderDTO> get(String id){
        List<Order> orders = orderRepository.findOrdersByUser_Id(id);
        return orders.stream()
                .map(OrderMapper::toDTO)
                .collect(Collectors.toList());
    }

    public String change(OrderToChange data){
        try{
            Optional<Order> result = orderRepository.findOrderByIdAndUserId(data.getOrderId(), data.getUserId());

            if(result.isPresent()){
                Order order = result.get();

                order.setStatus(data.getStatus());

                orderRepository.save(order);

                return "Update order successfully";
            }
            else{
                return "Something is missing";
            }
        }
        catch (Exception e){
            return "Something is error: " + e.getMessage();
        }
    }
}
