package com.virtualnfc.backendproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.virtualnfc.backendproject.dto.PageRequestDTO;
import com.virtualnfc.backendproject.model.PageData;
import com.virtualnfc.backendproject.repository.PageDataRepository;
import com.virtualnfc.backendproject.service.FileStorageService;

import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import java.util.*;
import java.time.LocalDateTime;
import java.time.Duration;

import java.nio.file.*;
import java.io.File;
//@CrossOrigin(origins = "http://localhost:4200") // Apenas UM
@CrossOrigin(origins = "${FRONTEND_URL:http://localhost:4200}")

@RestController
@RequestMapping("/api/pages")
public class CartoesController {

    private final PageDataRepository repository;
    private final FileStorageService fileStorageService;
    private static final Logger log = LoggerFactory.getLogger(CartoesController.class);
    public CartoesController(PageDataRepository repository, FileStorageService fileStorageService) {
        this.repository = repository;
        this.fileStorageService = fileStorageService;
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
@PutMapping(
    value = "/update/{id}",
    consumes = MediaType.MULTIPART_FORM_DATA_VALUE
)
public ResponseEntity<PageData> updatePage(
        @PathVariable Long id,

        @RequestPart("dados") PageRequestDTO dto,
        @RequestPart(value = "logo", required = false) MultipartFile logoFile,
        @RequestPart(value = "background", required = false) MultipartFile bgFile
) {

    log.info("🔄 Update page iniciado - id={}", id);

    log.debug("📦 DTO recebido: nomeCartao={}, instagram={}, youtube={}, backgroundColor={}",
            dto.getNomeCartao(),
            dto.getInstagram(),
            dto.getYoutube(),
            dto.getBackgroundColor()
    );

    log.debug("🖼️ Logo recebido: {}",
            (logoFile != null ? logoFile.getOriginalFilename() : "NÃO ENVIADO"));

    log.debug("🎨 Background recebido: {}",
            (bgFile != null ? bgFile.getOriginalFilename() : "NÃO ENVIADO"));
    
    PageData page = repository.findById(id)
            .orElseThrow(() -> {
                log.warn("❌ Página não encontrada - id={}", id);
                return new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Página não encontrada");
            });

    log.debug("📄 Página carregada do banco - id={}, nomeCartao={}",
            page.getId(), page.getNomeCartao());

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

    if (logoFile != null && !logoFile.isEmpty()) {
        page.setLogoPath(fileStorageService.saveFile(logoFile, "logo", id));
    }

    if (bgFile != null && !bgFile.isEmpty()) {
        page.setBackgroundPath(fileStorageService.saveFile(bgFile, "bg", id));
    }

    return ResponseEntity.ok(repository.save(page));
}




}
