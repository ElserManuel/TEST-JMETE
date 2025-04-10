package pe.edu.vallegrande.proyect.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.proyect.modal.Persona;
import pe.edu.vallegrande.proyect.service.PersonaService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/personas")
public class PersonaController {
    private final PersonaService personaService;
    private static final String EXPECTED_TOKEN = "testtoken";
    private static final String AUTH_HEADER = "Authorization";

    @Autowired
    public PersonaController(PersonaService personaService) {
        this.personaService = personaService;
    }

    private Mono<ResponseEntity<?>> unauthorizedResponse() {
        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Unauthorized: Invalid or missing token"));
    }

    @GetMapping
    public Flux<Persona> getAllPersonas(@RequestHeader(value = AUTH_HEADER, required = true) String token) {
        if (!EXPECTED_TOKEN.equals(token)) {
            throw new UnauthorizedException("Invalid token");
        }
        return personaService.findAll()
                .sort(Comparator.comparing(Persona::getId));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Persona>> getPersonaById(
            @PathVariable Long id,
            @RequestHeader(value = AUTH_HEADER, required = true) String token) {
        if (!EXPECTED_TOKEN.equals(token)) {
            throw new UnauthorizedException("Invalid token");
        }
        return personaService.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<Persona> createPersona(
            @RequestBody Persona persona,
            @RequestHeader(value = AUTH_HEADER, required = true) String token) {
        if (!EXPECTED_TOKEN.equals(token)) {
            throw new UnauthorizedException("Invalid token");
        }
        return personaService.save(persona);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Persona>> updatePersona(
            @PathVariable Long id,
            @RequestBody Persona persona,
            @RequestHeader(value = AUTH_HEADER, required = true) String token) {
        if (!EXPECTED_TOKEN.equals(token)) {
            throw new UnauthorizedException("Invalid token");
        }
        persona.setId(id);
        return personaService.save(persona)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePersona(
            @PathVariable Long id,
            @RequestHeader(value = AUTH_HEADER, required = true) String token) {
        if (!EXPECTED_TOKEN.equals(token)) {
            throw new UnauthorizedException("Invalid token");
        }
        return personaService.deleteById(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    private static class UnauthorizedException extends RuntimeException {
        public UnauthorizedException(String message) {
            super(message);
        }
    }
}