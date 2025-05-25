package com.example.brokerage.controller;

import com.example.brokerage.entity.Asset;
import com.example.brokerage.repository.AssetRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
public class AssetController {

  private final AssetRepository assetRepository;

  @GetMapping
  public List<Asset> listAssets(@RequestParam Long customerId) {
    return assetRepository.findByCustomerId(customerId);
  }
}
