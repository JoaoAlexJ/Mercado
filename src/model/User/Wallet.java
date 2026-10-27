package model.User;

import java.math.BigDecimal;
import java.util.UUID;

public class Wallet {

    private final UUID id;
    private final UUID idUser;
    private BigDecimal balance;

    public Wallet(UUID id, UUID idUser) {

        this.id = id;
        this.idUser = idUser;
        this.balance = BigDecimal.ZERO;

    }

    //-----------------//

    public UUID getId() {
        return id;
    }

    public UUID getIdUser() {
        return idUser;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    //-----------------//

    public void credit(BigDecimal amount){
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("The credit amount must be greater than 0");

        }

        this.balance = this.balance.add(amount);
    }

    public void debit(BigDecimal amount){

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("The debit amount must be greater than 0");
        }

        if (this.balance.compareTo(amount) < 0 ){
            throw new IllegalArgumentException("Insufficient funds");
        }

        this.balance = this.balance.subtract(amount);
    }
}
