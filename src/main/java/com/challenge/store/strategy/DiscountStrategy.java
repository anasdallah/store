package com.challenge.store.strategy;


import java.math.BigDecimal;

public interface DiscountStrategy {

    BigDecimal applyDiscount(BigDecimal amount);
}