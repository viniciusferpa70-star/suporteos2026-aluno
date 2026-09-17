package com.curso.petshop.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "tutor")
public class Tutor {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 120)
    private String nome;
    @Column(nullable = false, unique = true, length = 160)
    private String email;
    @Column(nullable = false, length = 20)
    private String telefone;

    protected Tutor() {}
    public Tutor(String nome, String email, String telefone) {
        if (nome == null || nome.isBlank() || email == null || email.isBlank()
                || telefone == null || telefone.isBlank())
            throw new IllegalArgumentException("Nome, e-mail e telefone são obrigatórios");
        this.nome = nome.trim();
        this.email = email.trim().toLowerCase(java.util.Locale.ROOT);
        this.telefone = telefone.trim();
    }
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }
}
