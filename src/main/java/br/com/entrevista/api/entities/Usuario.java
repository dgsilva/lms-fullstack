package br.com.entrevista.api.entities;


import jakarta.persistence.*;
import java.time.*;
import java.util.*;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 80)
    private String primeiroNome;

    @Column(nullable = false, length = 120)
    private String ultimoNome;

    @Column(nullable = false)
    private LocalDate dataNascimento;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 30)
    private String telefone;

    @Column(nullable = false)
    private String senha; // armazenar com BCrypt

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuario_papeis",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "papel_id")
    )
    private Set<Roles> role = new HashSet<>();
}

