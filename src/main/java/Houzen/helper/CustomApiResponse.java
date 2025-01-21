package Houzen.helper;

public class CustomApiResponse {
    private String message;
    private boolean success;
    private Object data;  // Optional
    private int statusCode;
    private String jwtToken;  // Optional

    // Constructor for mandatory fields only
    public CustomApiResponse(String message, boolean success, int statusCode) {
        this.message = message;
        this.success = success;
        this.statusCode = statusCode;
        this.data = null;  // Optional field, defaults to null
        this.jwtToken = null;  // Optional field, defaults to null
    }

    // Constructor for all fields
    public CustomApiResponse(String message, boolean success, Object data, int statusCode, String jwtToken) {
        this.message = message;
        this.success = success;
        this.data = data;
        this.statusCode = statusCode;
        this.jwtToken = jwtToken;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
