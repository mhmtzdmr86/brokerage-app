package com.example.brokerage.controller;

import com.example.brokerage.dto.CreateOrderRequest;
import com.example.brokerage.dto.OrderResponse;
import com.example.brokerage.service.OrderService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @PostMapping
  public OrderResponse createOrder(@RequestBody CreateOrderRequest request) {
    return orderService.createOrder(request);
  }

  @GetMapping
  public List<OrderResponse> listOrders(
      @RequestParam Long customerId,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
    return orderService.listOrders(customerId, from, to);
  }

  @DeleteMapping("/{orderId}")
  public void cancelOrder(@PathVariable Long orderId) {
    orderService.cancelOrder(orderId);
  }
}
