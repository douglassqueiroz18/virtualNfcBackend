package com.virtualnfc.backendproject.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class PagBankService {

    private final RestTemplate rest = new RestTemplate();

    @Value("${pagbank.token}")
    private String token;

    public Map<String, Object> criarPagamentoPix(Double valor, String descricao) {

        String url = "https://sandbox.api.pagseguro.com/orders";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();

        body.put("reference_id", "ref-" + System.currentTimeMillis());

        // 🔥 ATENÇÃO: PAGBANK EXIGE CPF/CNPJ!
        Map<String, Object> customer = new HashMap<>();
        customer.put("name", "Cliente Teste");
        customer.put("email", "cliente@example.com");
        customer.put("tax_id", "12345678909"); // <- CPF VÁLIDO (FAKE) PARA TESTE
        body.put("customer", customer);

        body.put("items", List.of(
            Map.of(
                "name", descricao,
                "quantity", 1,
                "unit_amount", (int)(valor * 100)
            )
        ));

        body.put("qr_codes", List.of(
            Map.of(
                "amount", Map.of(
                    "value", (int)(valor * 100)
                )
            )
        ));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        return rest.postForObject(url, request, Map.class);
    }


    public Map<String, Object> consultarStatus(String orderId) {
        String url = "https://sandbox.api.pagseguro.com/orders/" + orderId;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = rest.exchange(url, HttpMethod.GET, request, Map.class);

        return response.getBody();
    }
}
