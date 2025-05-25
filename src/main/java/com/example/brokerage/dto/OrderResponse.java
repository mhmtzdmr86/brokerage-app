package com.example.brokerage.dto;

import com.example.brokerage.enums.OrderSide;
import com.example.brokerage.enums.OrderStatus;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponse {
  private Long id;
  private Long customerId;
  private String assetName;
  private OrderSide orderSide;
  private double size;
  private double price;
  private OrderStatus status;
  private LocalDateTime createDate;
}
