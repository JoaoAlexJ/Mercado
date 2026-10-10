package services;

import model.User;
import repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //-------------//

    public User registerUser(UserRequestDTO userRequestDTO){

        if (userRepository.findByEmail(userRequestDTO.email).isPresent()){
            throw new IllegalArgumentException("Email already register in system");
        }


        User newUser = new User(
                userRequestDTO.id,
                userRequestDTO.name,
                userRequestDTO.email,
                userRequestDTO.passWord,
                userRequestDTO.userType);

        userRepository.save(newUser);
        return newUser;

    }

    public User findById(UUID id){

        return userRepository.findByID(id).orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    public User findByEmail(String email){

        return userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    public List<User> findAllUsers(){
        if (userRepository.findAll().isEmpty()){
            System.err.println("no registered users");
        }

        return userRepository.findAll();
    }

    public void deleteById(UUID id){

        if (findById(id) == null){
            throw new NoSuchElementException("User not fund");
        }

        userRepository.delete(id);

    }

    public void deleteByEmail(String email){
        if (findByEmail(email) == null){
            throw new NoSuchElementException("User not found");
        }

        userRepository.deleteByEmail(email);
    }




}
