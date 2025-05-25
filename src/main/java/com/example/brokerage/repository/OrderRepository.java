package com.example.brokerage.repository;

import com.example.brokerage.entity.Order;
import com.example.brokerage.enums.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
  List<Order> findByCustomerIdAndCreateDateBetween(
      Long customerId, LocalDateTime from, LocalDateTime to);

  List<Order> findByStatus(OrderStatus status);
}
