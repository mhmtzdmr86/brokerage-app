package com.example.brokerage.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.brokerage.dto.CreateOrderRequest;
import com.example.brokerage.entity.Asset;
import com.example.brokerage.entity.Order;
import com.example.brokerage.enums.OrderSide;
import com.example.brokerage.enums.OrderStatus;
import com.example.brokerage.repository.AssetRepository;
import com.example.brokerage.repository.OrderRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

public class OrderServiceTest {

  @InjectMocks private OrderService orderService;

  @Mock private OrderRepository orderRepository;

  @Mock private AssetRepository assetRepository;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void createOrder_whenBuyWithSufficientTRY_shouldCreatePendingOrder() {
    // Arrange
    CreateOrderRequest request = new CreateOrderRequest();
    request.setCustomerId(1L);
    request.setAssetName("AAPL");
    request.setOrderSide(OrderSide.BUY);
    request.setSize(10);
    request.setPrice(100); // total 1000

    Asset tryAsset =
        Asset.builder().id(1L).customerId(1L).assetName("TRY").size(2000).usableSize(2000).build();

    when(assetRepository.findByCustomerIdAndAssetName(1L, "TRY")).thenReturn(Optional.of(tryAsset));

    when(orderRepository.save(any(Order.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    when(assetRepository.save(any(Asset.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    // Act
    var response = orderService.createOrder(request);

    // Assert
    assertEquals(OrderStatus.PENDING, response.getStatus());
    assertEquals("AAPL", response.getAssetName());
    assertEquals(1000, 2000 - tryAsset.getUsableSize()); // usableSize düşmüş olmalı

    verify(assetRepository, times(1)).save(any());
    verify(orderRepository, times(1)).save(any());
  }

  @Test
  public void createOrder_whenBuyWithoutSufficientTRY_shouldThrow() {
    // Arrange
    CreateOrderRequest request = new CreateOrderRequest();
    request.setCustomerId(1L);
    request.setAssetName("AAPL");
    request.setOrderSide(OrderSide.BUY);
    request.setSize(10);
    request.setPrice(500); // total 5000

    Asset tryAsset = Asset.builder().customerId(1L).assetName("TRY").usableSize(1000).build();

    when(assetRepository.findByCustomerIdAndAssetName(1L, "TRY")).thenReturn(Optional.of(tryAsset));

    // Act & Assert
    Exception exception =
        assertThrows(IllegalStateException.class, () -> orderService.createOrder(request));

    assertTrue(exception.getMessage().contains("Insufficient TRY"));
  }
}
