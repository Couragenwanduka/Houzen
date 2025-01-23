package Houzen.Response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import Houzen.helper.CustomApiResponse;

public class BuildErrorResponse {
// Helper method for building error responses
    public  ResponseEntity<CustomApiResponse> buildErrorResponse(String message, HttpStatus status) {
        CustomApiResponse response = new CustomApiResponse(
                message,
                false,
                null,
                status.value(),
                ""
        );
        return ResponseEntity.status(status).body(response);
    }
}
