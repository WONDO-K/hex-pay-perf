package com.example.hexpay.hexpay_perf.domain.model;

public enum PaymentStatus {
    // 결제 상태
    PENDING, // 결제 대기 중
    COMPLETED, // 결제 완료
    FAILED, // 결제 실패
    REFUNDED, // 환불됨
    CANCELED // 취소됨
}
