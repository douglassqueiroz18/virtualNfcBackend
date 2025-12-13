package com.virtualnfc.backendproject.controller;

import com.virtualnfc.backendproject.dto.PageRequestDTO;
import com.virtualnfc.backendproject.model.PageData;
import com.virtualnfc.backendproject.repository.PageDataRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.time.LocalDateTime;
import java.time.Duration;

//@CrossOrigin(origins = "http://localhost:4200") // Apenas UM
@CrossOrigin(origins = "${FRONTEND_URL:http://localhost:4200}")

@RestController
@RequestMapping("/api/pages")
public class CartoesController {

    private final PageDataRepository repository;

    public CartoesController(PageDataRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/create")
    public ResponseEntity<PageData> createPage(@RequestBody PageRequestDTO dto) {
        try {
            PageData page = new PageData();

            page.setType(dto.getType());
            page.setNomeCartao(dto.getNomeCartao());
            page.setInstagram(dto.getInstagram());
            page.setWhatsapp(dto.getWhatsapp());
            page.setFacebook(dto.getFacebook());
            page.setLinkedin(dto.getLinkedin());
            page.setTiktok(dto.getTiktok());
            page.setYoutube(dto.getYoutube());
            page.setSite(dto.getSite());
            page.setPrototipo(dto.getPrototipo());
            PageData saved = repository.save(page);

            return ResponseEntity.status(HttpStatus.CREATED).body(saved);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/all")
    public List<PageData> getAllPages() {
        return repository.findAll();
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        repository.deleteById(id);
        return "Removido: " + id;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<PageData> getById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/prototipo/get/{id}")
    public ResponseEntity<PageData> getByPrototipoById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/access/{serialKey}")
    public ResponseEntity<?> accessBySerial(@PathVariable String serialKey) {
    Optional<PageData> optional = repository.findBySerialKey(serialKey);

    if (optional.isEmpty()) {
        return ResponseEntity.status(404).body(Map.of(
            "valid", false,
            "message", "Chave de acesso inválida."
        ));
    }

    return ResponseEntity.ok(Map.of(
        "valid", true,
        "page", optional.get()
    ));
}
    /*@GetMapping("/prototipo/check/{id}")
public ResponseEntity<?> checkPrototipo(@PathVariable Long id) {
    Optional<PageData> optional = repository.findById(id);

    if (optional.isEmpty()) {
        return ResponseEntity.status(404).body(Map.of("expired", 1));
    }

    PageData page = optional.get();

    // NÃO É protótipo
    if (!"1".equals(page.getPrototipo())) {
        return ResponseEntity.ok(Map.of(
                "prototype", 0,
                "expired", 0
        ));
    }

    // É protótipo → calcular expiração
    LocalDateTime created = page.getCreatedAt();
    LocalDateTime expiresAt = created.plusHours(24);
    LocalDateTime now = LocalDateTime.now();

    boolean isExpired = now.isAfter(expiresAt);

    // SE EXPIRADO → DELETAR
    if (isExpired) {
        repository.deleteById(id);
        return ResponseEntity.ok(Map.of(
                "prototype", 1,
                "expired", 1,
                "deleted", true,
                "remainingSeconds", 0,
                "message", "Protótipo expirado e removido."
        ));
    }

    // NÃO expirou → calcular quanto falta
    long remaining = Duration.between(now, expiresAt).toSeconds();
    if (remaining < 0) remaining = 0;

    return ResponseEntity.ok(Map.of(
            "prototype", 1,
            "expired", 0,
            "deleted", false,
            "remainingSeconds", remaining,
            "expireAt", expiresAt.toString(),
            "page", page
    ));
}*/
@PutMapping("/update/{id}")
public ResponseEntity<?> updatePage(
        @PathVariable Long id,
        @RequestBody PageRequestDTO dto
) {
    Optional<PageData> optional = repository.findById(id);

    if (optional.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("message", "Página não encontrada.")
        );
    }
    if (dto.getLogoBase64() != null && dto.getLogoBase64().length() > 1_500_000) {
    return ResponseEntity.badRequest().body(
        Map.of("message", "Logo excede o tamanho permitido.")
    );
    }


    PageData page = optional.get();

    // Atualiza apenas os campos que existem no DTO
    page.setNomeCartao(dto.getNomeCartao());
    page.setInstagram(dto.getInstagram());
    page.setWhatsapp(dto.getWhatsapp());
    page.setFacebook(dto.getFacebook());
    page.setLinkedin(dto.getLinkedin());
    page.setTiktok(dto.getTiktok());
    page.setYoutube(dto.getYoutube());
    page.setSite(dto.getSite());
    page.setType(dto.getType());
    page.setPrototipo(dto.getPrototipo());
    page.setBackgroundColor(dto.getBackgroundColor());
    if (dto.getLogoBase64() != null) {
        page.setLogoBase64(dto.getLogoBase64());
    }
    PageData updated = repository.save(page);

    return ResponseEntity.ok(
            Map.of(
                    "message", "Página atualizada com sucesso.",
                    "page", updated
            )
    );
}


}
