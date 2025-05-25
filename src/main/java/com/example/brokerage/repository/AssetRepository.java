package com.example.brokerage.repository;

import com.example.brokerage.entity.Asset;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetRepository extends JpaRepository<Asset, Long> {
  List<Asset> findByCustomerId(Long customerId);

  Optional<Asset> findByCustomerIdAndAssetName(Long customerId, String assetName);
}
