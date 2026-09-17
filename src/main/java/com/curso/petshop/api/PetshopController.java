package com.curso.petshop.api;

import com.curso.petshop.application.PetshopService;
import static com.curso.petshop.api.PetshopDto.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PetshopController {
    private final PetshopService service;
    public PetshopController(PetshopService service) { this.service=service; }
    @GetMapping("/health") public String health() { return "OK"; }
    @PostMapping("/tutores") public ResponseEntity<TutorResponse> cadastrarTutor(@Valid @RequestBody TutorRequest r) {
        var t=service.cadastrarTutor(r); return ResponseEntity.created(URI.create("/api/tutores/"+t.id())).body(t);
    }
    @GetMapping("/tutores") public List<TutorResponse> tutores() { return service.listarTutores(); }
    @GetMapping("/tutores/{id}") public TutorResponse tutor(@PathVariable Long id) { return service.buscarTutor(id); }
    @PostMapping("/pets") public ResponseEntity<PetResponse> cadastrarPet(@Valid @RequestBody PetRequest r) {
        var p=service.cadastrarPet(r); return ResponseEntity.created(URI.create("/api/pets/"+p.id())).body(p);
    }
    @GetMapping("/pets") public List<PetResponse> pets() { return service.listarPets(); }
    @GetMapping("/pets/{id}") public PetResponse pet(@PathVariable Long id) { return service.buscarPet(id); }
    @PostMapping("/atendimentos") public ResponseEntity<AtendimentoResponse> agendar(@Valid @RequestBody AtendimentoRequest r) {
        var a=service.agendar(r); return ResponseEntity.created(URI.create("/api/atendimentos/"+a.id())).body(a);
    }
    @GetMapping("/atendimentos") public List<AtendimentoResponse> atendimentos() { return service.listarAtendimentos(); }
    @GetMapping("/atendimentos/{id}") public AtendimentoResponse atendimento(@PathVariable Long id) { return service.buscarAtendimento(id); }
    @PatchMapping("/atendimentos/{id}/concluir") public AtendimentoResponse concluir(@PathVariable Long id) { return service.concluir(id); }
}
