package com.example.colorapi.repository;

import com.example.colorapi.model.Saludo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaludoRepository extends JpaRepository<Saludo, Long> {

    List<Saludo> findTop20ByOrderByCreadoEnDesc();
}
