package model;

import java.util.Objects;
import java.util.UUID;

public record UserRequestDTO(String name, String email, String password) {

    public UserRequestDTO {

        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        if (email == null || email.isBlank()){
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        if (password == null || password.isBlank()){
            throw new IllegalArgumentException("Password cannot be null or empty");
        }


        name = name.trim();
        email = email.trim();
        password = password.trim();
    }
}
