package Houzen.helper;

public class OtpUtils {

    public static String generateOtp() {
        int randomPin   =(int) (Math.random()*9000)+1000;
        return String.valueOf(randomPin);
    }

}
