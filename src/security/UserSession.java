package security;

import model.User.User;
import model.User.UserType;

import java.util.Objects;

public class UserSession {

    private static User currentUser;

    private UserSession() {

    }

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

    public static boolean isAuthenticated(){
        return currentUser != null;
    }

    public static boolean isAdm(){
        return currentUser.getUserType() == UserType.ADM;
    }

}
