# 💳 외화 결제 시스템 성능 테스트 프로젝트 hex-pay-perf

## 📌 프로젝트 개요

이 프로젝트는 외화 기반 결제 시스템을 **헥사고날 아키텍처** 기반으로 설계하고,  
핵심 로직과 I/O 연산에 대한 **성능 테스트 및 최적화 실험**을 진행하기 위한 토이 프로젝트입니다.

> 실무 경험 부족 영역이었던 **성능 개선**을 실습하고,  
> **MSA로 확장 가능한 구조**를 미리 설계하는 것이 주요 목표입니다.

---

## 🧩 핵심 기능 및 비즈니스 정책

`- 외화 결제 요청을 처리하고, 원화로 환산된 금액을 반환
- 실시간 환율을 외부 API를 통해 조회하며, 10분간 캐싱 적용
- 결제 시 수수료 1.5% 부과
- 계산된 결제 금액은 30분간 유효
- 오류 조건: 존재하지 않는 통화코드, 환율 API 실패 등`

## 🚀 정책 사항
| 항목        | 정책 내용                       | 비고               |
| --------- | --------------------------- | ---------------- |
| 📌 지원 통화  | USD, EUR, JPY, KRW          |                  |
| 💱 환율 캐시  | 10분                         | Caffeine 적용      |
| 💸 수수료    | 결제 금액의 1.5%                 | 고정               |
| 🕒 유효시간   | 30분                         | LocalDateTime 기준 |
| 🧾 소수점 처리 | 소수점 2자리, 반올림                | BigDecimal 사용    |
| 🔒 오류 처리  | API 실패 시 Fallback, 잘못된 통화코드 | 표준 에러            |
| 🏦 결제 상태  | READY, COMPLETED, FAILED    | Enum             |
| 🎁 적립 정책  | 결제 금액에 따라 1~3% 차등 적립           | 환율 변환 후 원화 기준    |
| 🎉 할인 정책  | 기본 미적용 → 이벤트 시 적용           | 유연한 확장 고려        |
| 🔄 중복 처리  | 주문번호 기반                     | Idempotency 가능   |

## 💡 적립 포인트 정책
| 결제 금액 (원화 기준) | 적립 비율      | 비고      |
| ------------- | ---------- | ------- |
| 전 고객          | 1% 적립      | 고정      |
| 향후 이벤트성 할인    | 별도 정책으로 분리 | Ongoing |

---

## 🧱 디렉토리 구조 (예정)

```
src
└── main
    └── java/com/example/payment
        ├── application         # 유스케이스 계층 (PaymentService)
        ├── domain              # 도메인 모델 및 Port (in/out)
        │   ├── model
        │   └── port
        ├── infrastructure      # 어댑터 구현 (API, Cache 등)
        ├── web                 # REST 컨트롤러 계층
        └── config              # 스프링 설정 파일
└── docker/
    ├── docker-compose.mysql.yml
    ├── docker-compose.artillery.yml
    ├── docker-compose.logging.yml
    ├── run-artillery.sh
    └── artillery/
└── load-tests/
    ├── load-test.yml
    ├── result.json
    └── result.html
└── test
    ├── benchmark               # JMH 기반 성능 테스트
    └── unit                    # 단위 테스트
```

---

## 🧪 성능 측정 대상 및 목적

| 테스트 항목           | 설명                                | 도구                        |
| ---------------- | --------------------------------- | ------------------------- |
| 🔹 외부 API 호출 시간  | RestTemplate vs WebClient 비교      | Log + Timer               |
| 🔹 결제 계산 로직      | 대량 호출 시 처리량                       | **JMH**                   |
| 🔹 환율 캐시 전략 비교   | `ConcurrentHashMap` vs `Caffeine` | JMH + 로그 비교               |
| 🔹 대량 DB 저장 처리   | 10만 건 주문 저장 시 성능 측정               | **Artillery** + DB 인덱스 실험 |
| 🔹 API TPS 한계 측정 | `/payments/prepare` 부하 시 응답 변화    | **Artillery**             |


---

## 🚀 실행 방법

```bash
# 빌드 및 실행
./gradlew build
./gradlew bootRun

# JMH 벤치마크 실행
./gradlew jmh

# Artillery 부하 테스트는 load-tests 디렉토리 참고
```

---

## 📬 API 예시

### ▶️ POST `/payments/prepare`

**요청 예시:**
```json
{
  "orderId": 1234,
  "currency": "USD",
  "amount": 100.00
}
```

**응답 예시:**
```json
{
  "orderId": 1234,
  "currency": "USD",
  "exchangeRate": 1321.75,
  "convertedAmount": 134157.63,
  "validUntil": "2025-06-06T13:48:00"
}
```

---

## 📊 도메인 정책 요약

- 환율 API 결과 캐시: 10분
- 결제 유효 시간: 30분
- 수수료 비율: 1.5%
- 소수점 반올림: 소수점 2자리

---

## 🛠 TODO

- [x] 도메인 설계 및 정책 정의  
- [x] 기본 프로젝트 구조 생성  
- [ ] PaymentService 유스케이스 구현  
- [ ] 외부 API 어댑터 + 캐시 전략 추가  
- [ ] JMH 테스트 구성  
- [ ] Artillery 스크립트 작성
- [ ] 로그: Logstash + Elasticsearch 연동
- [ ] 테스트 결과 README에 기록

---

## 📦 로그 아키텍처 (Observability 구성)

이 프로젝트는 로그 수집 및 분석을 위해 다음과 같은 **ELK (Elasticsearch, Logstash, Kibana)** 기반 파이프라인을 구성합니다.

```
Spring Boot (Logback → JSON 로그)
  ↓
Logstash (필터링 & 전달)
  ↓
Elasticsearch (로그 저장 & 검색)
```

### ✅ 구성 요소

| 구성요소              | 역할                                          | 사용 여부  |
| ----------------- | ------------------------------------------- | ------ |
| **Logback**       | JSON 구조화 로그 생성 (`logstash-logback-encoder`) | 사용     |
| **Logstash**      | 로그 수집 및 필터링, Elasticsearch로 전달              | 사용     |
| **Elasticsearch** | 로그 인덱싱 및 검색 엔진                              | 사용     |
| ~~**Kibana**~~    | ~~로그 시각화, 대시보드 구성~~                         | 미사용 예정 |


### 🧪 주요 활용

- 성능 테스트 시 요청 로그 시각화
- `exchangeRate`, `convertedAmount` 기준 검색
- 에러/예외 추적, API별 처리 시간 분석

> 이 구성을 통해 **실시간 성능 모니터링 및 문제 분석 역량**을 실습합니다.

---

## 🚀 실행 방법 (최신)
```bash
# 1️⃣ MySQL 실행
docker-compose -f docker/docker-compose.mysql.yml up -d

# 2️⃣ Spring Boot 실행
./gradlew bootRun

# 3️⃣ Artillery 테스트 실행
docker-compose -f docker/docker-compose.artillery.yml up --build

```


## 🧠 학습 및 실험 목적

- 헥사고날 아키텍처 기반 실용적 설계
- 성능 테스트 실전 적용 경험 확보
- 마이크로서비스 전환에 유리한 구조 체득
- 실무 인터뷰용 포트폴리오 완성도 향상

---

## ✍️ 작성자

- 최동호
- Spring + 실전 성능 개선 도전 중 🚀
