package com.virtualnfc.backendproject.controller;

import com.virtualnfc.backendproject.service.PagBankService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/pagbank")
public class PagamentoController {

    private final PagBankService pagBankService;

    public PagamentoController(PagBankService pagBankService) {
        this.pagBankService = pagBankService;
    }

    @PostMapping("/pix")
    public ResponseEntity<Map<String, Object>> gerarPix(@RequestBody Map<String, Object> request) {

        if (!request.containsKey("description") || !request.containsKey("value")) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Campos 'description' e 'value' são obrigatórios."
            ));
        }

        String descricao = request.get("description").toString();
        Double valor = Double.parseDouble(request.get("value").toString());

        Map<String, Object> resposta = pagBankService.criarPagamentoPix(valor, descricao);
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/pix/{orderId}")
    public ResponseEntity<Map<String, Object>> consultar(@PathVariable String orderId) {
        Map<String, Object> status = pagBankService.consultarStatus(orderId);
        return ResponseEntity.ok(status);
    }
    @GetMapping("/public-key")
    public ResponseEntity<Map<String, Object>> getPublicKey() {
        String key = pagBankService.getPublicKey();
        return ResponseEntity.ok(Map.of("public_key", key));
    }


}
