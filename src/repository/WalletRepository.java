package repository;

import model.Wallet;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends Repository<Wallet>{

    Optional<Wallet> findByIDUser(UUID id);


}
