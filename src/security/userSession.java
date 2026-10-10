package security;

import model.User;
import model.UserType;

import java.util.Objects;

public class userSession {

    private static User currentUser;

    public static void login(User user){
        currentUser = Objects.requireNonNull(user, "User cannot be null");
    }

    public static void logout(){
        if (currentUser == null){
            throw new RuntimeException("No logged-in users");
        }

        currentUser = null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public boolean isAuthenticated(){
        return currentUser != null;
    }

    public boolean isAdm(){
        return currentUser.getUserType() == UserType.ADM;
    }

}
