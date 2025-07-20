package com.example.hexpay.hexpay_perf.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

/**
 * Payment = 결제 정보
 *
 * 결제는 주문에 대한 결제 정보를 나타내며, 결제 금액, 화폐 단위, 환율 정보 등을 포함합니다.
 * 결제 상태는 PaymentStatus 열거형을 사용하여 관리합니다.
 *
 * 거래가 완료되었을 때 저장되는 불변 기록 -> final로 선언
 */
@Builder
@AllArgsConstructor
@Getter
public class Payment {
    private final Long orderId; // 주문 ID
    private final Currency currency; // 결제 화폐 단위
    private final BigDecimal amount; // 결제 금액
    private final BigDecimal exchangeRate; // 환율 정보
    private final BigDecimal convertedAmount; // 환전된 금액
    private final BigDecimal fee; // 결제 수수료
    private final BigDecimal point; // 결제 포인트
    private final LocalDateTime createdAt; // 결제 생성 시간
    private final LocalDateTime validUntil; // 결제 유효 시간
    private PaymentStatus status; // 결제 상태


    private BigDecimal calculateConvertedAmount() {
        // 환전된 금액 계산
        return amount.multiply(exchangeRate).setScale(2, RoundingMode.HALF_UP);
    }

    // 결제 수수료 계산
    private BigDecimal calculateFee() {
        return convertedAmount.multiply(new BigDecimal("0.015")).setScale(2, RoundingMode.HALF_UP);
    }

    // 결제 상태 업데이트
    public void updateStatus(PaymentStatus newStatus) {

        this.status = newStatus;
    }
}
