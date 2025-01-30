package Houzen.controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
// import com.cloudinary.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import Houzen.Response.InternalError;
import Houzen.helper.CustomApiResponse;
import Houzen.helper.JwtUtils;
import Houzen.model.PropertyModel;
import Houzen.model.User;
import Houzen.service.PropertyService;
import Houzen.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/property")
public class PropertyController {
    
    @Value("${file.upload-dir}")
    private String uploadDir;
    private final PropertyService propertyService;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(PropertyController.class);

    @Autowired
    public PropertyController(PropertyService propertyService, UserService userService) {
        this.propertyService = propertyService;
        this.userService = userService;
    }

    private ResponseEntity<CustomApiResponse> buildUnauthorizedResponse() {
        CustomApiResponse response = new CustomApiResponse(
            "Unauthorized",
            false,
            null,
            HttpStatus.UNAUTHORIZED.value(),
            ""
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

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

    @PostMapping("/add")
    public ResponseEntity<CustomApiResponse> requireProperty(@Valid @RequestBody @RequestParam("file") MultipartFile file, PropertyModel propertyModel, HttpServletRequest request) {
        try {
            if (file.isEmpty()) {
                return buildErrorResponse("Please select a file to upload", HttpStatus.BAD_REQUEST);
            }
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return buildUnauthorizedResponse();
            }

            String token = authorizationHeader.substring(7);
            Map<String, Object> claims;
            try {
                claims = JwtUtils.validateToken(token);
            } catch (Exception e) {
                logger.error("Invalid JWT token", e);
                return buildUnauthorizedResponse();
            }

            if (claims == null || !"admin".equals(claims.get("role"))) {
                return buildUnauthorizedResponse();
            }

            String email = (String) claims.get("email");
            User existingUser = userService.findUserByEmail(email);
            if (existingUser == null) {
                return buildUnauthorizedResponse();
            }
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            } 
            String fileName = file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            file.transferTo(filePath.toFile());
           


            PropertyModel property = propertyService.savePropertyModel(propertyModel);
            CustomApiResponse response = new CustomApiResponse(
                "Property added successfully",
                true,
                property,
                HttpStatus.CREATED.value(),
                ""
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            logger.error("Error occurred while adding property: {}", e.getMessage(), e);
            return InternalError.buildInternalErrorResponse();
     }
     
   }
   
}
