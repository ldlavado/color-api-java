package com.example.colorapi.repository;

import com.example.colorapi.model.ColorGenerado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ColorGeneradoRepository extends JpaRepository<ColorGenerado, Long> {

    List<ColorGenerado> findTop20ByOrderByCreadoEnDesc();
}
