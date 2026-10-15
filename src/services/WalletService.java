package services;

import model.User;
import model.Wallet;
import repository.UserRepository;
import repository.WalletRepository;

import java.math.BigDecimal;
import java.util.List;
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

        userRepository.findByID(idUser).orElseThrow(() -> new NoSuchElementException("User not found."));


        Optional<Wallet> wallet = walletRepository.findByIDUser(idUser);

        if (wallet.isPresent()){
            Wallet w = wallet.get();
            throw new SecurityException("This user already has registered Wallet. Walled ID: "+w.getId());
        }

        Wallet newWallet = new Wallet(UUID.randomUUID(), idUser);
        walletRepository.save(newWallet);
        return newWallet;
    }

    public Wallet findByID(UUID id){

        if (id == null){
            throw new IllegalArgumentException("Invalid ID ");
        }

        return walletRepository.findByID(id).orElseThrow(() -> new NoSuchElementException("Wallet not found."));

    }

    public Wallet findByUserID(UUID id){

        if (id == null){
            throw new IllegalArgumentException("Invalid ID ");
        }

        return walletRepository.findByIDUser(id).orElseThrow(() -> new NoSuchElementException("Wallet not found."));

    }

    public List<Wallet> findAll(){

        return walletRepository.findAll();
    }

    public void deleteByID(UUID id){
        walletRepository.delete(id);
    }

    //----------------------//

    public void deposit(UUID idUser, BigDecimal amount){

        Wallet wallet = findByUserID(idUser);
        wallet.credit(amount);
        walletRepository.save(wallet);

    }

    public void withdraw(UUID idUser, BigDecimal amount){

        Wallet wallet = findByUserID(idUser);
        wallet.debit(amount);
        walletRepository.save(wallet);

    }

    public BigDecimal getBalance(UUID idUser){

        return findByUserID(idUser).getBalance();
    }

}
