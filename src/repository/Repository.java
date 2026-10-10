package repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Repository<T> {

    void save(T obj);
    void delete(UUID id);
    Optional<T> findByID(UUID id);
    List<T> findAll();
}
