package app.com.shoppingapp.service;

import app.com.shoppingapp.dto.*;
import app.com.shoppingapp.repository.*;
import app.com.shoppingapp.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductVariantsRepository productVariantsRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public List<CartToGet> get(String userid){
        return cartRepository.getAllCart(userid);
    }

    public List<CartToGet> getFromOrder(List<OrderDTO> data, String orderId){
        List<CartToGet> items = new ArrayList<>();

        OrderDTO order = data.stream().filter(el -> el.getId().equals(orderId))
                .findFirst().orElse(null);

        if (order == null) return List.of();

        for(OrderItemDTO item : order.getItems()){
            CartToGet newItem = CartToGet.builder()
                    .id(item.getVariantId())
                    .color(item.getColor())
                    .price(item.getPrice())
                    .name(item.getProductName())
                    .size(item.getSize())
                    .quantity(item.getQuantity())
                    .imageUrl(item.getProductImage())
                    .build();

            items.add(newItem);
        }

        return items;
    }

    public String add(ProductAddToCart data){
        try {
            User user = userRepository.findUserById(data.getUserId());
            ProductVariant variant= productVariantsRepository.findByIdProductVariant(data.getProductVariantId());

            if(user == null || variant == null){
                return  "Có lỗi, vui lòng thử lại";
            }

            Product product = variant.getProduct();

            if(product.getStatus() == Product.Status.INACTIVE){
                return "Sản phẩm hiện tại đang tạm ngưng bán";
            }

            if(variant.getQuantity() == 0){
                return "Sản phẩm đã hết hàng";
            }

            Optional<Cart> result = cartRepository.findByIdProductVariantIdAndIdUserId(
                    data.getProductVariantId(),
                    data.getUserId()
            );

            if(result.isPresent())
            {
                Cart updateCart = result.get();
                int newQuantity = updateCart.getQuantity() + data.getQuantity();
                updateCart.setQuantity(newQuantity > variant.getQuantity() ? variant.getQuantity() : newQuantity);
                cartRepository.save(updateCart);
                return "Sản phẩm đã được cập nhật";
            }
            else{
                CartId id = new CartId(data.getProductVariantId(), data.getUserId());
                Cart cart = Cart.builder()
                    .id(id)
                    .user(user)
                    .productVariant(variant)
                    .quantity(data.getQuantity())
                    .createdAt(LocalDateTime.now())
                    .build();

                cartRepository.save(cart);
                return "Sản phẩm đã thêm vào giỏ hàng";
            }
        }
        catch (Exception e){
            return "Something is error: " + e.getMessage();
        }
    }

    public String delete(String productVariantId, String userId){
        try {
            CartId id = new CartId(productVariantId, userId);
            cartRepository.deleteById(id);
            return "Sản phẩm đã xóa khỏi giỏ hàng";
        }
        catch (Exception e){
            return "Lỗi: " + e.getMessage();
        }
    }

    public String order(String userId, String payment){
        try {
            User user = userRepository.findUserById(userId);
            Order.PaymentMethod method = Order.PaymentMethod.valueOf(payment);

            if(user == null){
                return "Something is missing, try again !";
            }

            Order newOrder = Order.builder()
                    .user(user)
                    .status("Pending")
                    .shippingFee(new BigDecimal("10000.00"))
                    .paymentMethod(method)
                    .createdAt(LocalDateTime.now())
                    .build();

            BigDecimal total = BigDecimal.ZERO;
            List<CartToGet> items = cartRepository.getAllCart(userId);

            if(items == null){
                return "Nothing can be order";
            }

            List<OrderItem> orderItems = new ArrayList<>();
            for(CartToGet item : items){
                ProductVariant variant = productVariantsRepository.findByIdProductVariant(item.getId());

                if(variant == null){
                    return "Missing something, try again";
                }

                if(variant.getQuantity() == 0){
                    continue;
                }

                BigDecimal itemPrice = variant.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                total = total.add(itemPrice);

                OrderItemId itemId = new OrderItemId(newOrder.getId(), variant.getIdProductVariant());

                OrderItem newItem = OrderItem.builder()
                        .orderId(itemId)
                        .productVariant(variant)
                        .order(newOrder)
                        .quantity(item.getQuantity())
                        .build();

                orderItems.add(newItem);

                variant.setQuantity(variant.getQuantity() - item.getQuantity());
                productVariantsRepository.save(variant);

                CartId id  = new CartId(item.getId(), userId);
                cartRepository.deleteById(id);
            }

            total = total.add(newOrder.getShippingFee());

            newOrder.setItems(orderItems);
            newOrder.setTotal(total);

            orderRepository.save(newOrder);

            return "Create new order successfully";
        }
        catch(Exception e) {
            return "Something is error: " + e.getMessage();
        }
    }

    public String update(CartToUpdate data){
        try{
            CartId id = new CartId(data.getProductVariantId(), data.getUserId());
            Optional<Cart> result = cartRepository.findById(id);

            if(result.isPresent()){
                Cart cart = result.get();
                cart.setQuantity(data.getQuantity());

                cartRepository.save(cart);

                return "Sản phẩm đã được cập nhật";
            }
            else{
                return "Lỗi, vui lòng thử lại !";
            }
        }
        catch (Exception e){
            return "Lỗi: " + e.getMessage();
        }
    }
}
