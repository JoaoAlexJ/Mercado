package model;

import java.util.UUID;

public class User {

    private UUID id;
    private String nome;
    private String email;
    private String senha;
    private UserType userType;

    public User(UUID id, String nome, String email, String senha, UserType userType) {

        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.userType = userType;
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public UserType getUserType() {
        return userType;
    }
}
