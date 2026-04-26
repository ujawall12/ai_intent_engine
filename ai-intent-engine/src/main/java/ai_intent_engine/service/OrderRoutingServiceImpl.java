package ai_intent_engine.service;

import ai_intent_engine.constant.IntentConstants;
import ai_intent_engine.entity.Order;
import ai_intent_engine.exception.OrderNotFoundException;
import ai_intent_engine.model.ApiResponse;
import ai_intent_engine.model.UserIntent;
import ai_intent_engine.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderRoutingServiceImpl implements OrderRoutingService {

    private final OrderRepository orderRepository;

    // Hardcoded for demo — in real system this comes from JWT token or session
    private static final String DEMO_CUSTOMER_ID = "CUST-123";

    @Override
    public ApiResponse route(UserIntent intent) {

        if (intent.getConfidence() < IntentConstants.CONFIDENCE_THRESHOLD) {
            log.warn("Low confidence: {}", intent.getConfidence());
            return ApiResponse.clarificationNeeded(
                    "I am not sure what you are asking. " +
                            "Could you please provide more details about your order?");
        }

        return switch (intent.getIntent()) {
            case IntentConstants.TRACK_ORDER -> handleTrackOrder(intent);
            case IntentConstants.CANCEL_ORDER -> handleCancelOrder(intent);
            case IntentConstants.REFUND_REQUEST -> handleRefundRequest(intent);
            default -> handleGeneralInquiry();
        };
    }

    private ApiResponse handleTrackOrder(UserIntent intent) {
        log.debug("Handling track order - orderId: {}, product: {}",
                intent.getOrderId(), intent.getProductName());

        // If specific orderId was extracted by AI use it
        if (intent.getOrderId() != null) {
            Order order = orderRepository.findByOrderId(intent.getOrderId())
                    .orElseThrow(() -> new OrderNotFoundException(
                            "No order found with ID: " + intent.getOrderId()));
            return buildTrackingResponse(order);
        }

        // If product name was extracted search by product
        if (intent.getProductName() != null) {
            List<Order> orders = orderRepository
                    .findByCustomerIdAndProductNameContainingIgnoreCase(
                            DEMO_CUSTOMER_ID, intent.getProductName());

            if (!orders.isEmpty()) {
                return buildTrackingResponse(orders.getFirst());
            }
        }

        // Otherwise return the most recent order
        Order latestOrder = orderRepository
                .findFirstByCustomerIdOrderByOrderDateDesc(DEMO_CUSTOMER_ID)
                .orElseThrow(() -> new OrderNotFoundException(
                        "No orders found for your account."));

        return buildTrackingResponse(latestOrder);
    }

    private ApiResponse handleCancelOrder(UserIntent intent) {
        log.debug("Handling cancel order request");

        Order order = findRelevantOrder(intent);

        if (order.getStatus().equals("DELIVERED")) {
            return ApiResponse.builder()
                    .success(false)
                    .intent(intent.getIntent())
                    .confidence(intent.getConfidence())
                    .message("Order ORD-" + order.getOrderId() +
                            " has already been delivered and cannot be cancelled. " +
                            "Please raise a refund request instead.")
                    .data(buildOrderData(order))
                    .build();
        }

        if (order.getStatus().equals("IN_TRANSIT")) {
            return ApiResponse.builder()
                    .success(false)
                    .intent(intent.getIntent())
                    .confidence(intent.getConfidence())
                    .message("Order for " + order.getProductName() +
                            " is already in transit and cannot be cancelled. " +
                            "Please refuse delivery or raise a return request.")
                    .data(buildOrderData(order))
                    .build();
        }

        // In real system you would update status to CANCELLED in DB here
        return ApiResponse.success(
                intent.getIntent(),
                intent.getConfidence(),
                "Your order for " + order.getProductName() +
                        " has been successfully cancelled. " +
                        "Refund of ₹" + order.getAmount() +
                        " will be processed within 5-7 business days.",
                buildOrderData(order)
        );
    }

    private ApiResponse handleRefundRequest(UserIntent intent) {
        log.debug("Handling refund request");

        Order order = findRelevantOrder(intent);

        if (!order.getStatus().equals("DELIVERED")) {
            return ApiResponse.builder()
                    .success(false)
                    .intent(intent.getIntent())
                    .confidence(intent.getConfidence())
                    .message("Refund can only be raised for delivered orders. " +
                            "Your order status is currently: " + order.getStatus())
                    .data(buildOrderData(order))
                    .build();
        }

        return ApiResponse.success(
                intent.getIntent(),
                intent.getConfidence(),
                "Refund request raised successfully for " +
                        order.getProductName() + ". Amount ₹" +
                        order.getAmount() +
                        " will be credited to your account within 5-7 business days.",
                buildOrderData(order)
        );
    }

    private ApiResponse handleGeneralInquiry() {
        List<Order> recentOrders = orderRepository
                .findByCustomerIdOrderByOrderDateDesc(DEMO_CUSTOMER_ID);

        return ApiResponse.success(
                IntentConstants.GENERAL_INQUIRY,
                1.0,
                "You have " + recentOrders.size() + " orders in your account. " +
                        "How can I help you today?",
                recentOrders.stream()
                        .map(this::buildOrderData)
                        .toList()
        );
    }

    private Order findRelevantOrder(UserIntent intent) {
        if (intent.getOrderId() != null) {
            return orderRepository.findByOrderId(intent.getOrderId())
                    .orElseThrow(() -> new OrderNotFoundException(
                            "No order found with ID: " + intent.getOrderId()));
        }

        if (intent.getProductName() != null) {
            List<Order> orders = orderRepository
                    .findByCustomerIdAndProductNameContainingIgnoreCase(
                            DEMO_CUSTOMER_ID, intent.getProductName());
            if (!orders.isEmpty()) {
                return orders.getFirst();
            }
        }

        return orderRepository
                .findFirstByCustomerIdOrderByOrderDateDesc(DEMO_CUSTOMER_ID)
                .orElseThrow(() -> new OrderNotFoundException(
                        "No orders found for your account."));
    }

    private ApiResponse buildTrackingResponse(Order order) {
        String statusMessage = switch (order.getStatus()) {
            case "PROCESSING" -> "Your order is being processed and will be shipped soon.";
            case "IN_TRANSIT" -> "Your order is on the way! " +
                    "Tracking number: " + order.getTrackingNumber() +
                    ". Expected delivery: " + order.getEstimatedDelivery()
                    .toLocalDate();
            case "DELIVERED" -> "Your order was delivered on " +
                    order.getEstimatedDelivery().toLocalDate() + ".";
            default -> "Order status: " + order.getStatus();
        };

        return ApiResponse.success(
                IntentConstants.TRACK_ORDER,
                1.0,
                statusMessage,
                buildOrderData(order)
        );
    }

    private Object buildOrderData(Order order) {
        return new LinkedHashMap<String, Object>() {{
            put("orderId", order.getOrderId());
            put("productName", order.getProductName());
            put("status", order.getStatus());
            put("orderDate", order.getOrderDate().toLocalDate());
            put("estimatedDelivery",
                    order.getEstimatedDelivery() != null ?
                            order.getEstimatedDelivery().toLocalDate() : "Not yet assigned");
            put("amount", "₹" + order.getAmount());
            put("trackingNumber",
                    order.getTrackingNumber() != null ?
                            order.getTrackingNumber() : "Not yet assigned");
        }};
    }
}
