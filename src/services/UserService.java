package services;

import model.User;
import model.dto.UserRequestDTO;
import model.UserType;
import repository.Interfaces.UserRepository;
import security.PasswordHasher;
import security.UserSession;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.function.Function;

public class UserService {

    private final UserRepository userRepository;
    private final WalletService walletService;
    private final Function<User, String> toUserName = User::getName;

    public UserService(UserRepository userRepository, WalletService walletService) {
        this.userRepository = userRepository;
        this.walletService = walletService;
    }

    //-------------//

    public User login(String email, String password){

        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        String hashPassword = PasswordHasher.hash(password);

        if (!user.getPassword().equals(hashPassword)) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        UserSession.login(user);
        return user;

    }


    public User registerCustomer(UserRequestDTO userRequestDTO){

        if (userRepository.findByEmail(userRequestDTO.email()).isPresent()){
            throw new IllegalArgumentException("Email already register in system");
        }

        UUID id = UUID.randomUUID();
        String hashPassword = PasswordHasher.hash(userRequestDTO.password());

        User newUser = new User(
                id,
                userRequestDTO.name(),
                userRequestDTO.email(),
                hashPassword,
                UserType.CUSTUMER);

        userRepository.save(newUser);
        walletService.walletRegister(newUser.getId());

        return newUser;

    }

    public User registerAdm(UserRequestDTO userRequestDTO){

        if (userRepository.findByEmail(userRequestDTO.email()).isPresent()){
            throw new IllegalArgumentException("Email already register in system");
        }

        if (!UserSession.isAdm()){
            throw new SecurityException("Only administrators can creat new Administrators");
        }

        UUID id = UUID.randomUUID();
        String hashpassword = PasswordHasher.hash(userRequestDTO.password());

        User newUser = new User(
                id,
                userRequestDTO.name(),
                userRequestDTO.email(),
                hashpassword,
                UserType.ADM
        );

        userRepository.save(newUser);
        return newUser;
    }

    public User findById(UUID id){

        return userRepository.findByID(id).orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    public User findByEmail(String email){

        boolean isSelf = UserSession.getCurrentUser().getEmail().equals(email);

        if (!UserSession.isAdm() && !isSelf){
            throw new SecurityException("Access denied: you can only viw your own profile");
        }

        return userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    public List<User> findAllUsers(){

        if (!UserSession.isAdm()){
            throw new SecurityException("Only administrators can do this");
        }

        if (userRepository.findAll().isEmpty()){
            System.out.println("no registered users");
        }

        return userRepository.findAll().stream()
                .sorted(Comparator.comparing(toUserName))
                .toList();
    }

    public List<User> findAllAdm(){

        return findAllUsers().stream()
                .filter(u -> u.getUserType().equals(UserType.ADM))
                .sorted(Comparator.comparing(toUserName))
                .toList();

    }

    public List<User> findAllCustomers(){

        return findAllUsers().stream()
                .filter(u -> u.getUserType().equals(UserType.CUSTUMER))
                .sorted(Comparator.comparing(toUserName))
                .toList();

    }

    public void deleteById(UUID id){

        if (!UserSession.isAdm()){
            throw new SecurityException("Only administrators can do this");
        }


        userRepository.delete(id);
        walletService.deleteByID(walletService.findByUserID(id).getId());

    }

    public User deleteByEmail(String email){
        if (!UserSession.isAdm()){
            throw new SecurityException("Only administrators can do this");
        }

        User user = findByEmail(email);

        userRepository.deleteByEmail(email);
        walletService.deleteByID(walletService.findByUserID(user.getId()).getId());
        return user;

    }

    //------------------------------------------------------//

    public void changePassword(String currentPassword, String newPassword){

        User currentUser = UserSession.getCurrentUser();
        if (currentUser == null){
            throw new SecurityException("No session active, please log in");
        }


        if (currentPassword == null || currentPassword.isBlank()){
            throw new IllegalArgumentException("Current password cannot be empty");
        }

        if (newPassword == null || newPassword.isBlank()){
            throw new IllegalArgumentException("newPassword cannot be empty");
        }

        //------------------//

        if (!PasswordHasher.checkPassword(currentPassword, currentUser.getPassword())){
            throw new IllegalArgumentException("Current password does't match");
        }

        if (currentPassword.equals(newPassword)){
            throw new IllegalArgumentException("The new password must be different from the current one");
        }

        String newPasswordHash = PasswordHasher.hash(newPassword);

        User userUpdated = new User(currentUser.getId(),
                currentUser.getName(),
                currentUser.getEmail(),
                newPasswordHash,
                currentUser.getUserType());

        userRepository.save(userUpdated);
        UserSession.login(userUpdated);
    }




}
