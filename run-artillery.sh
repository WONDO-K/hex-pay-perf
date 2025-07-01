#!/bin/bash

# 날짜 기준 디렉토리명 생성
NOW=$(date +'%Y-%m-%d')
REPORT_DIR="load-tests/reports/$NOW"

# 디렉토리 생성
mkdir -p "$REPORT_DIR"

# 테스트 실행 및 결과 저장
docker compose -f docker-compose.artillery.yml run --rm artillery

# 결과 파일 이동
mv load-tests/result.json "$REPORT_DIR/"
mv load-tests/result.html "$REPORT_DIR/"

# 요약 추출: Markdown 생성
TOTAL_REQ=$(jq '.aggregate.counters["http.requests"]' "$REPORT_DIR/result.json")
AVG_RES_TIME=$(jq '.aggregate.latency.mean' "$REPORT_DIR/result.json")
P95_RES_TIME=$(jq '.aggregate.latency["p95"]' "$REPORT_DIR/result.json")

cat <<EOF > "$REPORT_DIR/summary.md"
# 📊 Artillery 테스트 요약 (${NOW})

| 항목 | 값 |
|------|----|
| 총 요청 수 | $TOTAL_REQ |
| 평균 응답 시간 (ms) | $AVG_RES_TIME |
| 95% 응답 시간 (ms) | $P95_RES_TIME |

👉 [HTML 리포트 보기](./result.html)
EOF

echo "✅ 테스트 완료: $REPORT_DIR/summary.md"
