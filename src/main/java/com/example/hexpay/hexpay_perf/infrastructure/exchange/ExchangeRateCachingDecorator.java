package com.example.hexpay.hexpay_perf.infrastructure.exchange;

import com.example.hexpay.hexpay_perf.domain.model.Currency;
import com.example.hexpay.hexpay_perf.domain.port.ExchangeRateProvider;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class ExchangeRateCachingDecorator implements ExchangeRateProvider {

    private final ExchangeRateProvider target;

    // ✅ 통화별로 캐시하고, 캐시 정책 적용
    private final Cache<Currency, BigDecimal> cache = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .maximumSize(100)
            .build();

    @Override
    public BigDecimal getExchangeRate(Currency currency) throws IOException {
        // ✅ 캐시 miss 시만 호출 (동기적으로 처리됨)
        return cache.get(currency, key -> {
            try {
                return target.getExchangeRate(key);
            } catch (IOException e) {
                throw new RuntimeException("환율 조회 실패", e); // 캐시 로딩에서 예외 못 던지므로 Wrapping
            }
        });
    }
}
