package Houzen.controllers;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import Houzen.helper.CustomApiResponse;
import Houzen.helper.EmailSender;
import Houzen.helper.JwtUtils;
import Houzen.helper.OtpUtils;
import Houzen.model.User;
import Houzen.Response.InternalError;
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

    // Helper method for building error responses
    private ResponseEntity<CustomApiResponse> buildErrorResponse(String message, HttpStatus status) {
        CustomApiResponse response = new CustomApiResponse(
                message,
                false,
                null,
                status.value(),
                ""
        );
        return ResponseEntity.status(status).body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<CustomApiResponse> registerUser(@Valid @RequestBody User user) {
        try {
            // Check if the user already exists
            User existingUser = userService.findUserByEmail(user.getEmail());
            if (existingUser != null) {
                return buildErrorResponse("Email already exists", HttpStatus.BAD_REQUEST);
            }

            // Save user to the database
            User savedUser = userService.saveUser(user);

            // Generate OTP and token
            String otp = OtpUtils.generateOtp();
            Map<String, Object> claims = new HashMap<>();
            claims.put("otp", otp);
            claims.put("email", savedUser.getEmail());
            String token = JwtUtils.generateToken(savedUser.getEmail(), claims);

            // Send OTP via email
            EmailSender.sendOtpMail(savedUser.getEmail(), otp);

            // Build success response
            CustomApiResponse response = new CustomApiResponse(
                    "User created successfully",
                    true,
                    savedUser,
                    HttpStatus.CREATED.value(),
                    token
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            logger.error("Error occurred during user registration", e);
            return InternalError.buildInternalErrorResponse();
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody Map<String, String> otpData, HttpServletRequest request) {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return buildErrorResponse("Unauthorized access", HttpStatus.UNAUTHORIZED);
            }

            String token = authorizationHeader.substring(7);
            Map<String, Object> claims = JwtUtils.validateToken(token);

            String otp = (String) claims.get("otp");
            String email = (String) claims.get("email");

            if (otp == null || !otp.equals(otpData.get("otp"))) {
                return buildErrorResponse("Invalid OTP", HttpStatus.BAD_REQUEST);
            }

            User existingUser = userService.findUserByEmail(email);
            if (existingUser == null) {
                return buildErrorResponse("User does not exist", HttpStatus.BAD_REQUEST);
            }

            existingUser.setVerified(true);
            userService.saveUser(existingUser);

            return ResponseEntity.status(HttpStatus.OK).body("OTP verified successfully");

        } catch (Exception e) {
            logger.error("Error occurred during OTP verification", e);
            return InternalError.buildInternalErrorResponse();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<CustomApiResponse> loginUser(@Valid @RequestBody User user) {
        try {
            // Check if the user exists
            User existingUser = userService.findUserByEmail(user.getEmail());
            if (existingUser == null) {
                return buildErrorResponse("User does not exist", HttpStatus.BAD_REQUEST);
            }

            // Check if the user is verified
            if (!existingUser.isVerified()) {
                return buildErrorResponse("User is not verified", HttpStatus.BAD_REQUEST);
            }

            // Check password validity
            if (!userService.checkPassword(user.getPassword(), existingUser.getPassword())) {
                return buildErrorResponse("Invalid password", HttpStatus.BAD_REQUEST);
            }

            // Generate JWT token
            Map<String, Object> claims = new HashMap<>();
            claims.put("email", existingUser.getEmail());
            claims.put("role", existingUser.getRole());
            claims.put("id", existingUser.getId());

            String token = JwtUtils.generateToken("userDetails", claims);

            // Build success response
            CustomApiResponse response = new CustomApiResponse(
                    "User logged in successfully",
                    true,
                    existingUser,
                    HttpStatus.OK.value(),
                    token
            );

            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {
            logger.error("Error occurred during user login", e);
            return InternalError.buildInternalErrorResponse();
        }
    }
}
