package pe.edu.vallegrande.proyect.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.proyect.modal.Persona;
import pe.edu.vallegrande.proyect.service.PersonaService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;

@RestController
@CrossOrigin(origins = "*")
public class PersonaControllerTest {

    private final PersonaService personaService;

    @Autowired
    public PersonaControllerTest(PersonaService personaService) {
        this.personaService = personaService;
    }

    // --- VULNERABILIDAD API9:2023 - Improper Inventory Management ---
    // Endpoints de prueba expuestos públicamente sin autenticación ni control de acceso

    @GetMapping("/test")
    public Flux<Persona> getAllPersonasTest() {
        return personaService.findAll()
                .sort(Comparator.comparing(Persona::getId));
    }

    @PostMapping("/test/insert")
    public Mono<Persona> insertTestPersona(@RequestBody Persona persona) {
        return personaService.save(persona);
    }

    @DeleteMapping("/test/delete/{id}")
    public Mono<ResponseEntity<Object>> deleteTestPersona(@PathVariable Long id) {
        return personaService.deleteById(id)
                .then(Mono.just(ResponseEntity.noContent().build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
