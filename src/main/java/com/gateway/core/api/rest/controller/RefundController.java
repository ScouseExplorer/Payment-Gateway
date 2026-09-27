package com.gateway.core.api.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Refund Controller
 * 
 * Handles refund-related operations:
 * - Full refunds of completed transactions
 * - Partial refunds
 * - Refund status tracking
 * - Settlement reconciliation
 * 
 * REST Endpoints:
 * POST   /api/refunds              - Create a new refund request
 * GET    /api/refunds/{id}         - Get refund status
 * GET    /api/payments/{id}/refunds - Get all refunds for a payment
 * 
 * @author Payment Team
 */
@RestController
@RequestMapping("/api/refunds")
public class RefundController {

    /**
     * NOT YET IMPLEMENTED: correct refunds require the double-entry ledger
     * (Section 7, Stage 2) so a refund is never a bare status flip. Returning
     * 200 OK here would be a financial-integrity bug (a client would believe
     * money moved when it did not), so this deliberately returns 501 instead.
     */
    @PostMapping
    public ResponseEntity<?> createRefund(@RequestBody Object request) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    /**
     * NOT YET IMPLEMENTED: see {@link #createRefund}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getRefund(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
