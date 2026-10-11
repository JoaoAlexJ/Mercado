package security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class PasswordHasher {

    private PasswordHasher(){

    }

    public static String hash(String rawPassword){

        if (rawPassword == null || rawPassword.isBlank()){
            throw new IllegalArgumentException("rawPassword cannot be null.");
        }

        try{

            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte [] hashByte = digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(hashByte);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 is not available");
        }

    }

    public static boolean checkPassword(String rawPassword, String storedPassword){

        if (rawPassword == null || storedPassword == null){
            return false;
        }

        return hash(rawPassword).equals(storedPassword);

    }
}
