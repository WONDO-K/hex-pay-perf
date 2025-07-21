package com.example.hexpay.hexpay_perf.domain.port;

import com.example.hexpay.hexpay_perf.domain.model.Currency;

import java.io.IOException;
import java.math.BigDecimal;

public interface ExchangeRateProvider {
    BigDecimal getExchangeRate(Currency currency) throws IOException;
}
