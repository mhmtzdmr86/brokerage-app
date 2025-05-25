package com.example.brokerage.entity;

import com.example.brokerage.enums.OrderSide;
import com.example.brokerage.enums.OrderStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders") // 'order' SQL'de rezerve kelime
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long customerId;

  private String assetName;

  @Enumerated(EnumType.STRING)
  private OrderSide orderSide;

  private double size;

  private double price;

  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  private LocalDateTime createDate;
}
