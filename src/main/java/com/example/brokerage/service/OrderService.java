package com.example.brokerage.service;

import static java.util.stream.Collectors.toList;

import com.example.brokerage.dto.Constants;
import com.example.brokerage.dto.CreateOrderRequest;
import com.example.brokerage.dto.OrderResponse;
import com.example.brokerage.entity.Asset;
import com.example.brokerage.entity.Order;
import com.example.brokerage.enums.OrderSide;
import com.example.brokerage.enums.OrderStatus;
import com.example.brokerage.repository.AssetRepository;
import com.example.brokerage.repository.OrderRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final AssetRepository assetRepository;

  public OrderResponse createOrder(CreateOrderRequest request) {
    String tradedAsset = request.getAssetName();
    Long customerId = request.getCustomerId();

    if (request.getOrderSide() == OrderSide.BUY) {
      Asset tryAsset =
          assetRepository
              .findByCustomerIdAndAssetName(customerId, Constants.TRY)
              .orElseThrow(() -> new IllegalArgumentException(Constants.ASSET_NOT_FOUND_TRY));

      double totalCost = request.getSize() * request.getPrice();
      if (tryAsset.getUsableSize() < totalCost) {
        throw new IllegalStateException(Constants.INSUFFICIENT_BALANCE_TRY);
      }

      tryAsset.setUsableSize(tryAsset.getUsableSize() - totalCost);
      assetRepository.save(tryAsset);

    } else if (request.getOrderSide() == OrderSide.SELL) {
      Asset asset =
          assetRepository
              .findByCustomerIdAndAssetName(customerId, tradedAsset)
              .orElseThrow(() -> new IllegalArgumentException(Constants.ASSET_NOT_FOUND));

      if (asset.getUsableSize() < request.getSize()) {
        throw new IllegalStateException(Constants.INSUFFICIENT_ASSET_SIZE_TO_SELL);
      }

      asset.setUsableSize(asset.getUsableSize() - request.getSize());
      assetRepository.save(asset);
    }

    Order order =
        Order.builder()
            .customerId(request.getCustomerId())
            .assetName(request.getAssetName())
            .orderSide(request.getOrderSide())
            .size(request.getSize())
            .price(request.getPrice())
            .status(OrderStatus.PENDING)
            .createDate(LocalDateTime.now())
            .build();

    orderRepository.save(order);

    return mapToResponse(order);
  }

  public List<OrderResponse> listOrders(Long customerId, LocalDateTime from, LocalDateTime to) {
    return orderRepository.findByCustomerIdAndCreateDateBetween(customerId, from, to).stream()
        .map(this::mapToResponse)
        .collect(toList());
  }

  public void cancelOrder(Long orderId) {
    Order order =
        orderRepository
            .findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException(Constants.ORDER_NOT_FOUND));

    if (order.getStatus() != OrderStatus.PENDING) {
      throw new IllegalStateException(Constants.ONLY_PENDING_ORDERS_CAN_BE_CANCELLED);
    }

    // Geri yükleme işlemi
    Asset asset =
        assetRepository
            .findByCustomerIdAndAssetName(
                order.getCustomerId(),
                order.getOrderSide() == OrderSide.BUY ? Constants.TRY : order.getAssetName())
            .orElseThrow(() -> new IllegalArgumentException(Constants.ASSET_NOT_FOUND));

    double refund =
        order.getOrderSide() == OrderSide.BUY
            ? order.getSize() * order.getPrice()
            : order.getSize();

    asset.setUsableSize(asset.getUsableSize() + refund);
    assetRepository.save(asset);

    order.setStatus(OrderStatus.CANCELED);
    orderRepository.save(order);
  }

  private OrderResponse mapToResponse(Order order) {
    return OrderResponse.builder()
        .id(order.getId())
        .customerId(order.getCustomerId())
        .assetName(order.getAssetName())
        .orderSide(order.getOrderSide())
        .size(order.getSize())
        .price(order.getPrice())
        .status(order.getStatus())
        .createDate(order.getCreateDate())
        .build();
  }
}
