package com.example.hexpay.hexpay_perf.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// ExchangeRate = 화폐 환율 정보
public record ExchangeRate(
        Currency currency,
        BigDecimal rate,
        LocalDateTime fetchedAt
) {

}
