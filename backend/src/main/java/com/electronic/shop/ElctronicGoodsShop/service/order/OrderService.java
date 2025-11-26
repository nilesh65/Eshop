package com.electronic.shop.ElctronicGoodsShop.service.order;

import com.electronic.shop.ElctronicGoodsShop.dtos.OrderDto;
import com.electronic.shop.ElctronicGoodsShop.enums.OrderStatus;
import com.electronic.shop.ElctronicGoodsShop.model.*;
import com.electronic.shop.ElctronicGoodsShop.repository.OrderRepository;
import com.electronic.shop.ElctronicGoodsShop.repository.ProductRepository;
import com.electronic.shop.ElctronicGoodsShop.repository.UserRepository;
import com.electronic.shop.ElctronicGoodsShop.request.PaymentRequest;
import com.electronic.shop.ElctronicGoodsShop.service.cart.ICartService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ICartService cartService;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItemList = createOrderItems(order, cart);
        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalAmount(calculateTotalAmount(orderItemList));
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(cart.getId());
        return savedOrder;
    }

    private Order createOrder(Cart cart) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }
    @Transactional
    @Override
    public Order placeBuyNowOrder(Long userId, Long productId, int quantity) {
        // Fetch the user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Fetch the product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        // Create new order
        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());

        // Create order item for the Buy-Now product
        OrderItem orderItem = new OrderItem(order, product, product.getPrice(), quantity);

        // Reduce inventory
        product.setInventory(product.getInventory() - quantity);
        productRepository.save(product);

        // Set items and total amount
        order.setOrderItems(Set.of(orderItem));
        order.setTotalAmount(product.getPrice().multiply(BigDecimal.valueOf(quantity)));

        // Save and return the order
        return orderRepository.save(order);
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart) {
        return cart.getItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            product.setInventory(product.getInventory() - cartItem.getQuantity());
            productRepository.save(product);
            return new OrderItem(
                    order,
                    product,
                    cartItem.getUnitPrice(),
                    cartItem.getQuantity());
        }).toList();
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList) {
        return orderItemList.stream()
                .map(item -> item.getPrice()
                        .multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    @Override
    public List<OrderDto> getUserOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(this::convertToDto).toList();
    }
    @Override
    public String createPaymentIntent(PaymentRequest request) throws StripeException {
        long amountInSmallestUnit = Math.round(request.getAmount() * 100);
        PaymentIntent intent = PaymentIntent.create(
                PaymentIntentCreateParams.builder()
                        .setAmount(amountInSmallestUnit)
                        .setCurrency(request.getCurrency())
                        .addPaymentMethodType ( "card").build());
        return intent.getClientSecret();
}
    @Override
    public OrderDto convertToDto(Order order) {
        return modelMapper.map(order, OrderDto.class);
    }
}
