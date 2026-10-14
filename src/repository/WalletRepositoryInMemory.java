package repository;

import model.Wallet;

import java.util.*;

public class WalletRepositoryInMemory implements WalletRepository{

    private final Map<UUID, Wallet> walletMap = new HashMap<>();

    @Override
    public Optional<Wallet> findByIdUser(UUID id) {
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

        if (findByID(id).isEmpty()){
            throw new IllegalArgumentException("Wallet is not found");
        }

        walletMap.remove(id);
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
