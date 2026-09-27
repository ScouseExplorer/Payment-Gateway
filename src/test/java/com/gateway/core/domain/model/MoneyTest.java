package com.gateway.core.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MoneyTest {

    @Test
    void addingDifferentCurrenciesThrows() {
        Money gbp = new Money(100L, Currency.GBP);
        Money usd = new Money(100L, Currency.USD);

        assertThatThrownBy(() -> gbp.add(usd)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void addingSameCurrencySucceeds() {
        Money a = new Money(100L, Currency.GBP);
        Money b = new Money(50L, Currency.GBP);

        assertThat(a.add(b).getAmount()).isEqualTo(150L);
    }
}
