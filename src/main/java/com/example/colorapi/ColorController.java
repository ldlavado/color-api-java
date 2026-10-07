package com.example.colorapi;

import com.example.colorapi.model.ColorGenerado;
import com.example.colorapi.model.Saludo;
import com.example.colorapi.repository.ColorGeneradoRepository;
import com.example.colorapi.repository.SaludoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller
public class ColorController {

    private final SaludoRepository saludos;
    private final ColorGeneradoRepository colores;
    private final Random random = new Random();

    public ColorController(SaludoRepository saludos, ColorGeneradoRepository colores) {
        this.saludos = saludos;
        this.colores = colores;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("color", registrarColor("pagina"));
        model.addAttribute("saludos", saludos.findTop20ByOrderByCreadoEnDesc());
        return "color";
    }

    @GetMapping("/color")
    @ResponseBody
    public Map<String, String> getColor() {
        Map<String, String> response = new HashMap<>();
        response.put("color", registrarColor("api"));
        return response;
    }

    @PostMapping("/saludar")
    @ResponseBody
    public ResponseEntity<Map<String, String>> saludar(@RequestBody NombreRequest request) {
        String nombre = request == null ? null : request.getNombre();
        if (nombre == null || nombre.trim().isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("detail", "El nombre no puede estar vacio");
            return ResponseEntity.badRequest().body(error);
        }

        String limpio = nombre.trim();
        String color = registrarColor("saludo");
        Saludo saludo = new Saludo();
        saludo.setNombre(limpio);
        saludo.setSaludo("Hola " + limpio + "!");
        saludo.setColor(color);
        saludos.save(saludo);

        Map<String, String> response = new HashMap<>();
        response.put("saludo", saludo.getSaludo());
        response.put("color", color);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/saludos")
    @ResponseBody
    public Object listarSaludos() {
        return saludos.findTop20ByOrderByCreadoEnDesc();
    }

    @GetMapping("/colores")
    @ResponseBody
    public Object listarColores() {
        return colores.findTop20ByOrderByCreadoEnDesc();
    }

    private String registrarColor(String origen) {
        String color = generarColor();
        ColorGenerado generado = new ColorGenerado();
        generado.setColor(color);
        generado.setOrigen(origen);
        colores.save(generado);
        return color;
    }

    private String generarColor() {
        return String.format("#%06x", random.nextInt(0xFFFFFF + 1));
    }
}
