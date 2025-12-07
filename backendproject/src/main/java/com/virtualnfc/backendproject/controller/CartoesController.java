package com.virtualnfc.backendproject.controller;

import com.virtualnfc.backendproject.dto.PageRequestDTO;
import com.virtualnfc.backendproject.model.PageData;
import com.virtualnfc.backendproject.repository.PageDataRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200") // Apenas UM
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
}
