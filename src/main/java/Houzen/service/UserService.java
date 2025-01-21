package Houzen.service;
import  Houzen.model.User;
import Houzen.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bcryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.bcryptPasswordEncoder = new BCryptPasswordEncoder();
    }
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findUserByPhone(String phone) {
        return userRepository.findByPhoneNumber(phone);
    }
    public User saveUser(User user) {
        return userRepository.save(user);
    }
    public void deleteUser(User user) {
        userRepository.deleteById(user.getId());
    }
    public User updateUser(String id, User user) {
       User existingUser = userRepository.findById(id).orElse(null);
       if (existingUser != null) {
           existingUser.setFirstName(user.getFirstName());
           existingUser.setLastName(user.getLastName());
           existingUser.setEmail(user.getEmail());
           existingUser.setPhoneNumber(user.getPhoneNumber());
           existingUser.setLocation(user.getLocation());
           return userRepository.save(existingUser);
       }
       return null;
    }
    public String hashPassword(String password) {
        return bcryptPasswordEncoder.encode(password);
    }
    public boolean checkPassword(String rawPassword, String hashedPassword) {
        return bcryptPasswordEncoder.matches(rawPassword, hashedPassword);
    }

}
