package repository.Interfaces;

import model.User;

import java.util.Optional;

public interface UserRepository extends Repository<User> {

    Optional<User> findByEmail(String email);
    void deleteByEmail(String email);
}
