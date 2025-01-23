package Houzen.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import Houzen.helper.CustomApiResponse;
import Houzen.model.PropertyModel;
import Houzen.service.PropertyService;
import jakarta.validation.Valid;
import Houzen.helper.JwtUtils;
import Houzen.service.UserService;
import Houzen.model.User;
import Houzen.Response.InternalError;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/property")
public class PropertyController {

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

    @PostMapping("/add")
    public ResponseEntity<CustomApiResponse> requireProperty(@Valid @RequestBody PropertyModel propertyModel, HttpServletRequest request) {
        try {
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
