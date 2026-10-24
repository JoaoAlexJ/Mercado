package repository;

import model.Wallet;
import repository.Interfaces.WalletRepository;

import java.util.*;

public class WalletRepositoryInMemory implements WalletRepository {

    private final Map<UUID, Wallet> walletMap = new HashMap<>();

    @Override
    public Optional<Wallet> findByIDUser(UUID id) {
        if (id == null){
            return Optional.empty();
        }

        return walletMap.values().stream()
                .filter(w -> w.getIdUser().equals(id))
                .findFirst();
    }

    @Override
    public void save(Wallet obj) {
        if (obj == null){
            throw new IllegalArgumentException("Invalid wallet");
        }

        walletMap.put(obj.getId(), obj);
    }

    @Override
    public void delete(UUID id) {
        if (id == null){
            throw new IllegalArgumentException("Invalida ID");
        }

        Wallet walletRemoved = walletMap.remove(id);

        if (walletRemoved == null){
            throw new NoSuchElementException("Wallet not found with this ID: "+id);
        }
    }

    @Override
    public Optional<Wallet> findByID(UUID id) {
        return Optional.ofNullable(walletMap.get(id));
    }

    @Override
    public List<Wallet> findAll() {
        return new ArrayList<>(walletMap.values());
    }
}
