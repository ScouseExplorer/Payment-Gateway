package com.gateway.core.api.rest.controller;

import com.gateway.core.application.command.CancelPaymentHandler;
import com.gateway.core.application.command.CapturePaymentHandler;
import com.gateway.core.application.command.CreatePaymentHandler;
import com.gateway.core.application.command.PaymentCreationResult;
import com.gateway.core.application.query.PaymentQueryService;
import com.gateway.core.domain.model.Currency;
import com.gateway.core.domain.model.IdempotencyKey;
import com.gateway.core.domain.model.Merchant;
import com.gateway.core.domain.model.Money;
import com.gateway.core.domain.model.Payment;
import com.gateway.core.domain.model.PaymentId;
import com.gateway.core.domain.model.PaymentMethod;
import com.gateway.core.domain.model.PaymentMethodType;
import com.gateway.core.domain.model.RiskScore;
import com.gateway.core.domain.model.TransactionId;
import com.gateway.core.exception.PaymentNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreatePaymentHandler createPaymentHandler;
    @MockitoBean
    private CapturePaymentHandler capturePaymentHandler;
    @MockitoBean
    private CancelPaymentHandler cancelPaymentHandler;
    @MockitoBean
    private PaymentQueryService paymentQueryService;

    private Payment authorizedPayment() {
        Merchant merchant = new Merchant("merchant_1", "Acme", "5411", "GB00BANK", true);
        Payment payment = new Payment(PaymentId.generate(), IdempotencyKey.of("idem-1"), merchant,
                new Money(1000L, Currency.GBP), new PaymentMethod(PaymentMethodType.CARD, "tok_123", "4242"),
                "desc", "order_1", "buyer@example.com", "127.0.0.1");
        payment.authorize(TransactionId.of("txn_1"), RiskScore.of(0));
        return payment;
    }

    @Test
    void createPaymentRequiresIdempotencyKeyHeader() throws Exception {
        mockMvc.perform(post("/api/payments")
                        .contentType("application/json")
                        .content("""
                                {
                                  "merchantId": "merchant_1",
                                  "amount": 1000,
                                  "currency": "GBP",
                                  "paymentMethodType": "CARD",
                                  "paymentMethodToken": "tok_123"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createPaymentReturns201ForNewPayment() throws Exception {
        when(createPaymentHandler.handle(any())).thenReturn(new PaymentCreationResult(authorizedPayment(), true));

        mockMvc.perform(post("/api/payments")
                        .header("Idempotency-Key", "idem-1")
                        .contentType("application/json")
                        .content("""
                                {
                                  "merchantId": "merchant_1",
                                  "amount": 1000,
                                  "currency": "GBP",
                                  "paymentMethodType": "CARD",
                                  "paymentMethodToken": "tok_123"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("AUTHORIZED"));
    }

    @Test
    void getPaymentReturns404WhenMissing() throws Exception {
        when(paymentQueryService.getPayment(eq("missing"))).thenThrow(new PaymentNotFoundException("missing"));

        mockMvc.perform(get("/api/payments/missing"))
                .andExpect(status().isNotFound());
    }
}
