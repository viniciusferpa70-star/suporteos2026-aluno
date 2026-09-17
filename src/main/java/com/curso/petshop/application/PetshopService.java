package com.curso.petshop.application;

import com.curso.petshop.api.PetshopMapper;
import com.curso.petshop.domain.*;
import com.curso.petshop.repository.*;
import static com.curso.petshop.api.PetshopDto.*;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PetshopService {
    public static class NaoEncontrado extends RuntimeException {
        public NaoEncontrado(String mensagem) { super(mensagem); }
    }
    public static class Conflito extends RuntimeException {
        public Conflito(String mensagem) { super(mensagem); }
    }
    private final TutorRepository tutores;
    private final PetRepository pets;
    private final AtendimentoRepository atendimentos;
    private final PetshopMapper mapper;
    public PetshopService(TutorRepository tutores, PetRepository pets, AtendimentoRepository atendimentos, PetshopMapper mapper) {
        this.tutores=tutores; this.pets=pets; this.atendimentos=atendimentos; this.mapper=mapper;
    }
    @Transactional
    public TutorResponse cadastrarTutor(TutorRequest r) {
        String email=r.email().trim().toLowerCase(Locale.ROOT);
        if(tutores.existsByEmail(email)) throw new Conflito("E-mail de tutor já cadastrado");
        return mapper.response(tutores.save(new Tutor(r.nome(),email,r.telefone())));
    }
    public List<TutorResponse> listarTutores() { return tutores.findAll().stream().map(mapper::response).toList(); }
    public TutorResponse buscarTutor(Long id) { return mapper.response(tutor(id)); }
    private Tutor tutor(Long id) { return tutores.findById(id).orElseThrow(()->new NaoEncontrado("Tutor não encontrado")); }
    @Transactional
    public PetResponse cadastrarPet(PetRequest r) {
        return mapper.response(pets.save(new Pet(r.nome(),r.especie(),r.raca(),tutor(r.tutorId()))));
    }
    public List<PetResponse> listarPets() { return pets.findAll().stream().map(mapper::response).toList(); }
    public PetResponse buscarPet(Long id) { return mapper.response(pet(id)); }
    private Pet pet(Long id) { return pets.findById(id).orElseThrow(()->new NaoEncontrado("Pet não encontrado")); }
    @Transactional
    public AtendimentoResponse agendar(AtendimentoRequest r) {
        String codigo=r.codigo().trim();
        if(atendimentos.existsByCodigo(codigo)) throw new Conflito("Código de atendimento já cadastrado");
        return mapper.response(atendimentos.save(new Atendimento(codigo,pet(r.petId()),r.servico(),r.dataHora(),r.valor(),r.observacoes())));
    }
    private Atendimento atendimento(Long id) { return atendimentos.findById(id).orElseThrow(()->new NaoEncontrado("Atendimento não encontrado")); }
    public AtendimentoResponse buscarAtendimento(Long id) { return mapper.response(atendimento(id)); }
    public List<AtendimentoResponse> listarAtendimentos() { return atendimentos.findAll().stream().map(mapper::response).toList(); }
    @Transactional
    public AtendimentoResponse concluir(Long id) {
        Atendimento a=atendimento(id);
        if(a.getStatus()!=Atendimento.Status.AGENDADO) throw new Conflito("Atendimento já concluído");
        a.concluir();
        return mapper.response(a);
    }
}
