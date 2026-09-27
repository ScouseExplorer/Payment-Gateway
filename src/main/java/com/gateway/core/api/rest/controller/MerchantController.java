package com.gateway.core.api.rest.controller;

import com.gateway.core.api.rest.request.CreateMerchantRequest;
import com.gateway.core.api.rest.response.MerchantResponse;
import com.gateway.core.application.service.MerchantService;
import com.gateway.core.domain.model.Merchant;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Merchant Controller
 * 
 * Manages merchant accounts (Stage 1: registration and lookup only).
 * API key issuance, settlement configuration and limits are Stage 3+ concerns.
 * 
 * REST Endpoints:
 * POST   /api/merchants            - Register a new merchant
 * GET    /api/merchants/{id}       - Get merchant details
 * 
 * @author Payment Team
 */
@RestController
@RequestMapping("/api/merchants")
public class MerchantController {

    private final MerchantService merchantService;

    public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    /**
     * Register a new merchant.
     */
    @PostMapping
    public ResponseEntity<MerchantResponse> registerMerchant(@Valid @RequestBody CreateMerchantRequest request) {
        Merchant merchant = merchantService.register(new Merchant(request.getMerchantId(), request.getName(),
                request.getMcc(), request.getBankAccount(), request.getActive()));
        return ResponseEntity.status(HttpStatus.CREATED).body(MerchantResponse.from(merchant));
    }

    /**
     * Get merchant details.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MerchantResponse> getMerchant(@PathVariable String id) {
        return ResponseEntity.ok(MerchantResponse.from(merchantService.getMerchant(id)));
    }
}

