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

    public List<OrderDTO> get(){
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<OrderDTO> getById(String id){
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
    public long count(){
        return orderRepository.count();
    }

    public List<OrderDTO> search(String keyword){
        return orderRepository.searchOrders(keyword).stream()
                .map(OrderMapper::toDTO)
                .collect(Collectors.toList());
    }

    public long countByStatus(String status){
        return orderRepository.countByStatus(status);
    }

    public String updateStatus(String orderId, String newStatus) {
        try {
            // Validate input
            if (newStatus == null || newStatus.isBlank()) {
                return "Trạng thái không được để trống";
            }

            // Validate status value
            if (!List.of("Pending", "Confirmed", "Completed", "Cancelled").contains(newStatus)) {
                return "Trạng thái không hợp lệ";
            }

            Optional<Order> orderOpt = orderRepository.findById(orderId);

            if (orderOpt.isEmpty()) {
                return "Không tìm thấy đơn hàng";
            }

            Order order = orderOpt.get();
            String oldStatus = order.getStatus();

            // Nếu trạng thái giống nhau thì không cần update
            if (oldStatus.equals(newStatus)) {
                return "Trạng thái đã được cập nhật trước đó";
            }

            // Xử lý logic inventory khi chuyển sang Pending (đã ghi nhận)
            if (newStatus.equals("Pending") && !oldStatus.equals("Pending")) {
                List<OrderItem> items = order.getItems();
                for (OrderItem item : items) {
                    ProductVariant variant = item.getProductVariant();
                    variant.setQuantity(variant.getQuantity() - item.getQuantity());
                    productVariantsRepository.save(variant);
                }
            }

            // Xử lý logic inventory khi hủy đơn
            if (newStatus.equals("Cancelled") && oldStatus.equals("Pending")) {
                List<OrderItem> items = order.getItems();
                for (OrderItem item : items) {
                    ProductVariant variant = item.getProductVariant();
                    variant.setQuantity(variant.getQuantity() + item.getQuantity());
                    productVariantsRepository.save(variant);
                }
            }

            // Cập nhật trạng thái
            order.setStatus(newStatus);
            orderRepository.save(order);

            return "Cập nhật trạng thái thành công";
        } catch (Exception e) {
            return "Có lỗi xảy ra: " + e.getMessage();
        }
    }
}
