package com.example.brokerage.dto;

import com.example.brokerage.enums.OrderSide;
import lombok.Data;

@Data
public class CreateOrderRequest {
  private Long customerId;
  private String assetName;
  private OrderSide orderSide;
  private double size;
  private double price;
}
