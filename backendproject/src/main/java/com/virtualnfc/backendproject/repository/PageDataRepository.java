package com.virtualnfc.backendproject.repository;

import com.virtualnfc.backendproject.model.PageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.*;

public interface PageDataRepository extends JpaRepository<PageData, Long> {
    Optional<PageData> findBySerialKey(String serialKey);

    //@Query("SELECT p.id FROM PageData p")
    //List<Long> findAllIds();
}

