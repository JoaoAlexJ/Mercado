package repository;

import model.User;

import java.util.*;

public class UserRepositoryInMemory implements UserRepository{

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

        userMap.remove(id);
    }

    @Override
    public Optional<User> findByID(UUID id) {

        return Optional.ofNullable(userMap.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userMap.values().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public void deleteByEmail(String email) {

        userMap.values().removeIf(u -> u.getEmail().equalsIgnoreCase(email));
    }
}
