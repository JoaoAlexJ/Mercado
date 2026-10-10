package model;

import java.util.UUID;

public class User {

    private UUID id;
    private String nome;
    private String email;
    private String password;
    private UserType userType;

    public User(UUID id, String nome, String email, String password, UserType userType) {

        this.id = id;
        this.nome = nome;
        this.email = email;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public UserType getUserType() {
        return userType;
    }
}
