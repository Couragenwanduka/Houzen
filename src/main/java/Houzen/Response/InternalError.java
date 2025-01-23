package Houzen.Response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import Houzen.helper.CustomApiResponse;

public class InternalError {

    public static ResponseEntity<CustomApiResponse> buildInternalErrorResponse() {
        CustomApiResponse response = new CustomApiResponse(
            "Internal server error",
            false,
            null, 
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "" 
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
