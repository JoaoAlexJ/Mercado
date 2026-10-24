package repository.Interfaces;

import model.User.Wallet;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends Repository<Wallet>{

    Optional<Wallet> findByIDUser(UUID id);


}
