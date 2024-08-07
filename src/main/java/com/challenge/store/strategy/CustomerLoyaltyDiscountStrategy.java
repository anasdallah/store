package com.challenge.store.strategy;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

@Component
public class CustomerLoyaltyDiscountStrategy implements DiscountStrategy {

    @Override
    public BigDecimal applyDiscount(BigDecimal amount) {
        return amount.multiply(new BigDecimal("0.05"));
    }

}
