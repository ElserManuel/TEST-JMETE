package pe.edu.vallegrande.proyect.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.proyect.modal.Persona;
import pe.edu.vallegrande.proyect.repository.PersonaRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PersonaService {
    private final PersonaRepository personaRepository;

    @Autowired
    public PersonaService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    public Flux<Persona> findAll() {
        return personaRepository.findAll();
    }

    public Mono<Persona> findById(Long id) {
        return personaRepository.findById(id);
    }

    public Mono<Persona> save(Persona persona) {
        return personaRepository.save(persona);
    }

    public Mono<Void> deleteById(Long id) {
        return personaRepository.deleteById(id);
    }
}