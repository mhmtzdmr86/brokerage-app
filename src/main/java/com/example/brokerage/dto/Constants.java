package com.example.brokerage.dto;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
  public static final String TRY = "TRY";

  public static final String ORDER_NOT_FOUND = "Order not found";

  public static final String ONLY_PENDING_ORDERS_CAN_BE_CANCELLED =
      "Only PENDING orders can be cancelled";

  public static final String ASSET_NOT_FOUND = "Asset not found";

  public static final String ASSET_NOT_FOUND_TRY = "TRY asset not found";

  public static final String INSUFFICIENT_BALANCE_TRY = "Insufficient TRY balance";

  public static final String INSUFFICIENT_ASSET_SIZE_TO_SELL = "Insufficient asset size to sell";
}
