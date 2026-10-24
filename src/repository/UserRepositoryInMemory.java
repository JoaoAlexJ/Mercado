package repository;

import model.User;
import repository.Interfaces.UserRepository;

import java.util.*;

public class UserRepositoryInMemory implements UserRepository {

    private final Map<UUID, User> userMap = new HashMap<>();

    @Override
    public void save(User obj) {
        if (obj == null){
            throw new IllegalArgumentException("Invalid user");
        }

        userMap.put(obj.getId(), obj);
    }

    @Override
    public void delete(UUID id) {

        if (id == null){
            throw new IllegalArgumentException("Invalid ID: "+id);
        }

        User userRemoved = userMap.remove(id);

        if (userRemoved == null){
            throw new NoSuchElementException("Product not found with this ID: "+id);
        }

    }

    @Override
    public Optional<User> findByID(UUID id) {

        if (id == null){
            return Optional.empty();
        }

        return Optional.ofNullable(userMap.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public Optional<User> findByEmail(String email) {

        if (email == null || email.isBlank()){
            throw new IllegalArgumentException("Invalid Email: "+email);
        }

        return userMap.values().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public void deleteByEmail(String email) {

        userMap.values().removeIf(u -> u.getEmail().equalsIgnoreCase(email));
    }
}
