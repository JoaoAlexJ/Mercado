package services;

import model.Wallet;
import repository.UserRepository;
import repository.WalletRepository;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class WalletService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    public WalletService(WalletRepository walletRepository, UserRepository userRepository) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
    }

//--------------//

    public Wallet walletRegister(UUID idUser){

        if (userRepository.findByID(idUser).isEmpty()){
            throw new NoSuchElementException("User not found");
        }


        Optional<Wallet> wallet = walletRepository.findByIDUser(idUser);

        if (wallet.isPresent()){
            Wallet w = wallet.get();
            throw new SecurityException("This user already has registered Wallet. Walled ID: "+w.getId());
        }

        Wallet newWallet = new Wallet(UUID.randomUUID(), idUser);
        walletRepository.save(newWallet);
        return newWallet;
    }


}
