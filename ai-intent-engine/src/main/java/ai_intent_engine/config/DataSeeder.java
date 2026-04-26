package ai_intent_engine.config;

import ai_intent_engine.entity.Order;
import ai_intent_engine.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataSeeder {

    @Bean
    public CommandLineRunner seedData(OrderRepository orderRepository) {
        return args -> {
            if (orderRepository.count() == 0) {
                List<Order> orders = List.of(
                        Order.builder()
                                .orderId("ORD-001")
                                .customerId("CUST-123")
                                .productName("Nike Running Shoes")
                                .status("IN_TRANSIT")
                                .orderDate(LocalDateTime.now().minusDays(7))
                                .estimatedDelivery(LocalDateTime.now().plusDays(1))
                                .amount(4999.00)
                                .trackingNumber("TRK-789456123")
                                .build(),

                        Order.builder()
                                .orderId("ORD-002")
                                .customerId("CUST-123")
                                .productName("Adidas Sneakers")
                                .status("DELIVERED")
                                .orderDate(LocalDateTime.now().minusDays(14))
                                .estimatedDelivery(LocalDateTime.now().minusDays(7))
                                .amount(3499.00)
                                .trackingNumber("TRK-456123789")
                                .build(),

                        Order.builder()
                                .orderId("ORD-003")
                                .customerId("CUST-123")
                                .productName("Blue Denim Jacket")
                                .status("PROCESSING")
                                .orderDate(LocalDateTime.now().minusDays(1))
                                .estimatedDelivery(LocalDateTime.now().plusDays(5))
                                .amount(2999.00)
                                .trackingNumber(null)
                                .build(),

                        Order.builder()
                                .orderId("ORD-004")
                                .customerId("CUST-456")
                                .productName("Sony Headphones")
                                .status("IN_TRANSIT")
                                .orderDate(LocalDateTime.now().minusDays(3))
                                .estimatedDelivery(LocalDateTime.now().plusDays(2))
                                .amount(8999.00)
                                .trackingNumber("TRK-321654987")
                                .build()
                );

                orderRepository.saveAll(orders);
                log.info("Database seeded with {} orders", orders.size());
            }
        };
    }
}
