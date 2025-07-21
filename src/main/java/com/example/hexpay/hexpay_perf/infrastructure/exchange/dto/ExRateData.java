package com.example.hexpay.hexpay_perf.infrastructure.exchange.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true) // 생성자 안에 없는 값이 JSON에 들어있어도 무시하고 받아온다.
public record ExRateData(String result, Map<String, BigDecimal> rates) {

}