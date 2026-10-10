package model;

import java.util.UUID;

public class User {

    private UUID id;
    private String name;
    private String email;
    private String password;
    private UserType userType;

    public User(UUID id, String nome, String email, String senha, UserType userType) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.userType = userType;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
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
