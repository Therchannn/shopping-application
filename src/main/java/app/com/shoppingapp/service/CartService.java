package app.com.shoppingapp.service;

import app.com.shoppingapp.dto.*;
import app.com.shoppingapp.repository.OrderRepository;
import app.com.shoppingapp.entity.*;
import app.com.shoppingapp.mapper.CartMapper;
import app.com.shoppingapp.repository.CartRepository;
import app.com.shoppingapp.repository.ProductVariantsRepository;
import app.com.shoppingapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductVariantsRepository productVariantsRepository;
    private final OrderRepository orderRepository;

    public List<CartDTO> get(String id){
        List<Cart> carts = cartRepository.findCartsByIdUserId(id);
        return carts.stream()
                .map(CartMapper::toDTO)
                .collect(Collectors.toList());
    }

    public String add(ProductAddToCart data){
        try {
            User user = userRepository.findUserById(data.getUserId());
            ProductVariant variant= productVariantsRepository.findByIdProductVariant(data.getProductVariantId());

            if(user == null || variant == null){
                return  "Missing value, try again";
            }

            Optional<Cart> result = cartRepository.findCartByIdProductVariantId(data.getProductVariantId());
            if(result.isPresent())
            {
                Cart updateCart = result.get();
                updateCart.setQuantity(updateCart.getQuantity() + data.getQuantity());
                cartRepository.save(updateCart);
                return "Cart has been updated";
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
                return "Product has been added";
            }
        }
        catch (Exception e){
            return "Something is error: " + e.getMessage();
        }
    }

    public String delete(ProductDeleteFromCart data){
        try {
            CartId id = new CartId(data.getProductVariantId(), data.getUserId());
            cartRepository.deleteById(id);
            return "Product has been deleted";
        }
        catch (Exception e){
            return "Something is error: " + e.getMessage();
        }
    }

    public String order(CartToOrder data){
        try {
            User user = userRepository.findUserById(data.getUserId());

            if(user == null){
                return "Something is missing, try again !";
            }

            Order newOrder = Order.builder()
                    .user(user)
                    .status("Enrolled")
                    .shippingFee(new BigDecimal("10000.00"))
                    .paymentMethod(data.getPaymentMethod())
                    .createdAt(LocalDateTime.now())
                    .build();

            BigDecimal total = BigDecimal.ZERO;
            List<OrderItem> orderItems = new ArrayList<>();
            for(CartToOrder.productToOrder item : data.getProductVariants()){
                ProductVariant variant = productVariantsRepository.findByIdProductVariant(item.getProductVariantId());

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

                CartId id  = new CartId(item.getProductVariantId(), data.getUserId());
                cartRepository.deleteById(id);
            }

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

                return "Update cart successfully";
            }
            else{
                return "Something is missing, try again";
            }
        }
        catch (Exception e){
            return "Something is error: " + e.getMessage();
        }
    }
}
