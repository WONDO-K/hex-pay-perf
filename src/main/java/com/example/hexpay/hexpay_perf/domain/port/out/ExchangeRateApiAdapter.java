package com.example.hexpay.hexpay_perf.domain.port.out;

import com.example.hexpay.hexpay_perf.domain.model.Currency;
import com.example.hexpay.hexpay_perf.domain.port.ExchangeRateProvider;
import com.example.hexpay.hexpay_perf.infrastructure.exchange.dto.ExRateData;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;


@Component
@RequiredArgsConstructor
public class ExchangeRateApiAdapter implements ExchangeRateProvider {

    @Override
    public BigDecimal getExchangeRate(Currency currency) throws IOException {
        // https://open.er-api.com/v6/latest/USD
        URL url = new URL("https://open.er-api.com/v6/latest/" + currency);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(); // 접속해서 정보 받아오기 시작
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream())); //파일이나 네트워크로 통해 넘어온 정보를 바이트 형태로 리턴해줌
        String response = br.lines().collect(java.util.stream.Collectors.joining()); // string 타입으로 버퍼드리더에서 들어오는 것을 가져옴
        br.close();

        ObjectMapper mapper = new ObjectMapper();
        ExRateData data = mapper.readValue(response, ExRateData.class);
        System.out.println("API ExRate : " + data.rates().get("KRW"));
        return data.rates().get("KRW");
    }
}
