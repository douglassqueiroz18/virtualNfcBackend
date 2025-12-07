package com.virtualnfc.backendproject.controller;

import com.virtualnfc.backendproject.dto.ProdutoRequestDTO;
import com.virtualnfc.backendproject.model.Produto;
import com.virtualnfc.backendproject.repository.ProdutoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/products")
public class ProdutosController {

    private final ProdutoRepository repository;

    public ProdutosController(ProdutoRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/create")
    public ResponseEntity<Produto> createProduct(@RequestBody ProdutoRequestDTO dto) {
        Produto produto = new Produto();
        produto.setNome(dto.getNome());
        produto.setPreco(dto.getPreco());
        produto.setDescricao(dto.getDescricao());
        Produto saved = repository.save(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/all")
    public List<Produto> getAllProducts() {
        return repository.findAll();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Produto> getProductById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Produto> updateProduct(@PathVariable Long id, @RequestBody ProdutoRequestDTO dto) {
        return repository.findById(id).map(produto -> {
            produto.setNome(dto.getNome());
            produto.setPreco(dto.getPreco());
            produto.setDescricao(dto.getDescricao());
            Produto updated = repository.save(produto);
            return ResponseEntity.ok(updated);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.ok().body("Produto removido: " + id);
    }
}
