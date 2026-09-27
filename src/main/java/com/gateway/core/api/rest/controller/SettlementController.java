package com.gateway.core.api.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Settlement Controller
 * 
 * Manages settlement operations:
 * - Reconciliation of payments and refunds
 * - Settlement batch processing
 * - Fund transfer scheduling
 * - Settlement report generation
 * - Payout confirmations
 * 
 * REST Endpoints:
 * POST   /api/settlements          - Process a settlement batch
 * GET    /api/settlements/{id}     - Get settlement status
 * GET    /api/settlements          - List settlements with filters
 * GET    /api/settlements/{id}/reconciliation - Get reconciliation report
 * 
 * @author Payment Team
 */
@RestController
@RequestMapping("/api/settlements")
public class SettlementController {

    /**
     * NOT YET IMPLEMENTED: settlement is Stage 7 and depends on the ledger
     * (Stage 2) being in place first. Returns 501 rather than a false 200 OK.
     */
    @PostMapping
    public ResponseEntity<?> processSettlement() {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    /**
     * NOT YET IMPLEMENTED: see {@link #processSettlement}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getSettlement(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    /**
     * NOT YET IMPLEMENTED: see {@link #processSettlement}.
     */
    @GetMapping("/{id}/reconciliation")
    public ResponseEntity<?> getReconciliation(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
