package ai_intent_engine.repository;

import ai_intent_engine.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderId(String orderId);

    List<Order> findByCustomerIdOrderByOrderDateDesc(String customerId);

    List<Order> findByCustomerIdAndProductNameContainingIgnoreCase(
            String customerId, String productName);

    List<Order> findByCustomerIdAndOrderDateBetween(
            String customerId, LocalDateTime start, LocalDateTime end);

    Optional<Order> findFirstByCustomerIdOrderByOrderDateDesc(String customerId);
}
