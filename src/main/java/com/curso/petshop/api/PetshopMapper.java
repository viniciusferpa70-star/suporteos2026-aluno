package com.curso.petshop.api;
import com.curso.petshop.domain.*;
import static com.curso.petshop.api.PetshopDto.*;
import org.springframework.stereotype.Component;

@Component
public class PetshopMapper {
    public TutorResponse response(Tutor t) { return new TutorResponse(t.getId(), t.getNome(), t.getEmail(), t.getTelefone()); }
    public PetResponse response(Pet p) { return new PetResponse(p.getId(), p.getNome(), p.getEspecie(), p.getRaca(), p.getTutor().getId(), p.getTutor().getNome()); }
    public AtendimentoResponse response(Atendimento a) {
        return new AtendimentoResponse(a.getId(), a.getCodigo(), a.getPet().getId(), a.getPet().getNome(),
                a.getPet().getTutor().getId(), a.getPet().getTutor().getNome(), a.getServico(),
                a.getDataHora(), a.getValor(), a.getStatus(), a.getObservacoes());
    }
}
