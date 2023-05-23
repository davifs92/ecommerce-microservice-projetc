package com.davifs92.inventoryservice.controller;

import com.davifs92.inventoryservice.dto.InventoryResponse;
import com.davifs92.inventoryservice.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private InventoryService inventoryService;
    @GetMapping("/{sku-code}")
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCodes){
        return inventoryService.isInStock(skuCodes);

    }
}
