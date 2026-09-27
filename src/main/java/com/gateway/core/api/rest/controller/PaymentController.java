package com.gateway.core.api.rest.controller;

import com.gateway.core.api.rest.request.CreatePaymentRequest;
import com.gateway.core.api.rest.response.PaymentResponse;
import com.gateway.core.application.command.CancelPaymentCommand;
import com.gateway.core.application.command.CancelPaymentHandler;
import com.gateway.core.application.command.CapturePaymentCommand;
import com.gateway.core.application.command.CapturePaymentHandler;
import com.gateway.core.application.command.CreatePaymentCommand;
import com.gateway.core.application.command.CreatePaymentHandler;
import com.gateway.core.application.command.PaymentCreationResult;
import com.gateway.core.application.query.PaymentQueryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Payment Controller
 * 
 * Handles all payment-related HTTP requests including:
 * - Creating new payments (authorization)
 * - Capturing previously authorized payments
 * - Canceling pending payments
 * - Retrieving payment details
 * 
 * REST Endpoints:
 * POST   /api/payments              - Create a new payment
 * GET    /api/payments/{id}         - Get payment details
 * POST   /api/payments/{id}/capture - Capture an authorized payment
 * POST   /api/payments/{id}/cancel  - Cancel a pending/authorized payment
 * 
 * @author Payment Team
 */
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final CreatePaymentHandler createPaymentHandler;
    private final CapturePaymentHandler capturePaymentHandler;
    private final CancelPaymentHandler cancelPaymentHandler;
    private final PaymentQueryService paymentQueryService;

    public PaymentController(CreatePaymentHandler createPaymentHandler,
                              CapturePaymentHandler capturePaymentHandler,
                              CancelPaymentHandler cancelPaymentHandler,
                              PaymentQueryService paymentQueryService) {
        this.createPaymentHandler = createPaymentHandler;
        this.capturePaymentHandler = capturePaymentHandler;
        this.cancelPaymentHandler = cancelPaymentHandler;
        this.paymentQueryService = paymentQueryService;
    }

    /**
     * Create a new payment transaction. Requires an Idempotency-Key header (Section 6):
     * repeating the same key returns the original result instead of creating a duplicate.
     */
    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(
            @RequestHeader("Idempotency-Key") @NotBlank String idempotencyKey,
            @Valid @RequestBody CreatePaymentRequest request) {
        CreatePaymentCommand command = new CreatePaymentCommand(idempotencyKey, request.getMerchantId(),
                request.getAmount(), request.getCurrency(), request.getPaymentMethodType());
        command.setPaymentMethodToken(request.getPaymentMethodToken());
        command.setPaymentMethodLast4(request.getPaymentMethodLast4());
        command.setDescription(request.getDescription());
        command.setOrderId(request.getOrderId());
        command.setCustomerEmail(request.getCustomerEmail());
        command.setCustomerIp(request.getCustomerIp());

        PaymentCreationResult result = createPaymentHandler.handle(command);
        HttpStatus status = result.isNewlyCreated() ? HttpStatus.CREATED : HttpStatus.OK;
        return ResponseEntity.status(status).body(PaymentResponse.from(result.getPayment()));
    }

    /**
     * Retrieve payment details by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable String id) {
        return ResponseEntity.ok(PaymentResponse.from(paymentQueryService.getPayment(id)));
    }

    /**
     * Capture a previously authorized payment.
     */
    @PostMapping("/{id}/capture")
    public ResponseEntity<PaymentResponse> capturePayment(@PathVariable String id) {
        var payment = capturePaymentHandler.handle(new CapturePaymentCommand(id, null));
        return ResponseEntity.ok(PaymentResponse.from(payment));
    }

    /**
     * Cancel a pending or authorized payment.
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<PaymentResponse> cancelPayment(@PathVariable String id) {
        var payment = cancelPaymentHandler.handle(new CancelPaymentCommand(id, null));
        return ResponseEntity.ok(PaymentResponse.from(payment));
    }
}

