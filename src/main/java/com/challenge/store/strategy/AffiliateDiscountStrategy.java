package com.challenge.store.strategy;

import java.math.BigDecimal;

public class AffiliateDiscountStrategy implements DiscountStrategy {
    @Override
    public BigDecimal applyDiscount(BigDecimal amount) {
        return amount.multiply(new BigDecimal("0.10"));
    }

}
