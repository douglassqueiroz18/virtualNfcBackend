package com.virtualnfc.backendproject.repository;

import com.virtualnfc.backendproject.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;



import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}