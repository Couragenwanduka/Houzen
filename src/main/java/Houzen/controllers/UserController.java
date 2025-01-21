package Houzen.controllers;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Houzen.helper.CustomApiResponse;
import Houzen.helper.EmailSender;
import Houzen.helper.JwtUtils;
import Houzen.helper.OtpUtils;
import Houzen.model.User;
import Houzen.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<CustomApiResponse> registerUser(@Valid @RequestBody User user) {
        try {
            User existingUser = userService.findUserByEmail(user.getEmail());
            if (existingUser != null) {
                // Create a response with a failure message and send it back with BAD_REQUEST status
                CustomApiResponse response = new CustomApiResponse(
                        "Email already exists",
                        false,  
                        null,    
                        HttpStatus.BAD_REQUEST.value(),  
                        "" 
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
    
            // Save user to the database
            User savedUser = userService.saveUser(user);
            String otp = OtpUtils.generateOtp();
            Map<String, Object> claims = new HashMap<>();
            claims.put("otp", otp);
            claims.put("email", savedUser.getEmail());
            String token = JwtUtils.generateToken(savedUser.getEmail(), claims);
            EmailSender.sendOtpMail(savedUser.getEmail(), otp);
    
            // Create a response with success message and send it back with CREATED status
            CustomApiResponse response = new CustomApiResponse(
                    "User Created successfully",
                    true,    
                    savedUser,  
                    HttpStatus.CREATED.value(),  
                    token 
            );
    
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
    
        } catch (Exception e) {
            // Log the error and return a response with an INTERNAL_SERVER_ERROR status
            logger.error("An error occurred while registering the user", e);
    
            CustomApiResponse response = new CustomApiResponse(
                    "An error occurred while registering the user",
                    false,   
                    null,   
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),  
                    "" 
            );
    
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody Map<String, String> otpData, HttpServletRequest request) {
        try{
            String authorizationHeader = request.getHeader("Authorization");
            if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }

            String token = authorizationHeader.substring(7);
            Map<String, Object> claims = JwtUtils.validateToken(token);
            String otp = (String) claims.get("otp");
            String email = (String) claims.get("email");
            if(otp == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP");
            }
            if(!otp.equals(otpData.get("otp"))){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP");
            }

            User existingUser = userService.findUserByEmail(email);
            if(existingUser == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User does not exist");
            }
            
            existingUser.setVerified(true);
            userService.saveUser(existingUser);
            return ResponseEntity.status(HttpStatus.OK).body("OTP verified successfully");
        }catch(Exception e){
            logger.error("An error occurred while verifying the OTP", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while verifying the OTP");
        }

    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody User user) {
        try{
            User existingUser = userService.findUserByEmail(user.getEmail());
            if(existingUser == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("User does not exist");
            }
            if(!existingUser.isVerified()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("User is not verified");
            }
            if(!userService.checkPassword(user.getPassword(), existingUser.getPassword())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid password");
            }
            return ResponseEntity.status(HttpStatus.OK).body(existingUser);
        }catch(Exception e){
            logger.error("An error occurred while registering the user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while registering the user");
        }
    }

}
