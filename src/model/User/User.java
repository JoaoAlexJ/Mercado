package model.User;

import java.util.UUID;

public class User {

    private final UUID id;
    private final String name;
    private final String email;
    private final String password;
    private final UserType userType;

    public User(UUID id, String name, String email, String password, UserType userType) {

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
