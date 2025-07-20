package com.example.hexpay.hexpay_perf.domain.service;

import com.example.hexpay.hexpay_perf.domain.model.ExchangeRate;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PointPolicy {

    public static BigDecimal calculate(BigDecimal amountInWon) {
        // 단순 1% 포인트 정책
        return amountInWon.multiply(new BigDecimal("0.01")).setScale(0, RoundingMode.DOWN);
    }
}
