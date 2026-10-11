package services;

import model.User;
import model.UserRequestDTO;
import model.UserType;
import repository.UserRepository;
import security.UserSession;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //-------------//

    public User login(String email, String password){

        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!user.getPassword().equals(password)) {
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

        User newUser = new User(
                id,
                userRequestDTO.name(),
                userRequestDTO.email(),
                userRequestDTO.password(),
                UserType.CLIENTE);

        userRepository.save(newUser);
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
        User newUser = new User(
                id,
                userRequestDTO.name(),
                userRequestDTO.email(),
                userRequestDTO.password(),
                UserType.ADM
        );

        userRepository.save(newUser);
        return newUser;
    }

    public User findById(UUID id){

        boolean isSelf = UserSession.getCurrentUser().getId().equals(id);

        if (!UserSession.isAdm() && !isSelf){
            throw new SecurityException("Access denied: you can only viw your own profile");
        }

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
            System.err.println("no registered users");
        }

        return userRepository.findAll();
    }

    public void deleteById(UUID id){

        if (!UserSession.isAdm()){
            throw new SecurityException("Only administrators can do this");
        }


        findById(id);
        userRepository.delete(id);

    }

    public void deleteByEmail(String email){
        if (!UserSession.isAdm()){
            throw new SecurityException("Only administrators can do this");
        }

        findByEmail(email);
        userRepository.deleteByEmail(email);
    }




}
