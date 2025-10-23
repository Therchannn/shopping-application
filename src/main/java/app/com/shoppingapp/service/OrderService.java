package app.com.shoppingapp.service;

import app.com.shoppingapp.dto.OrderToChange;
import app.com.shoppingapp.entity.Order;
import app.com.shoppingapp.entity.OrderItem;
import app.com.shoppingapp.entity.ProductVariant;
import app.com.shoppingapp.mapper.OrderMapper;
import app.com.shoppingapp.repository.OrderRepository;
import app.com.shoppingapp.dto.OrderDTO;
import app.com.shoppingapp.repository.ProductVariantsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductVariantsRepository productVariantsRepository;

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

                if(order.getStatus().equals(data.getStatus())){
                    return "No thing can be updated";
                }

                if(data.getStatus().equals("Pending")){
                    List<OrderItem> items = order.getItems();

                    for(OrderItem item : items){
                        ProductVariant variant = item.getProductVariant();
                        variant.setQuantity(variant.getQuantity() - item.getQuantity());
                        productVariantsRepository.save(variant);
                    }
                }

                if(data.getStatus().equals("Cancelled")){
                    List<OrderItem> items = order.getItems();

                    for(OrderItem item : items){
                        ProductVariant variant = item.getProductVariant();
                        variant.setQuantity(variant.getQuantity() + item.getQuantity());
                        productVariantsRepository.save(variant);
                    }
                }

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
