package encryption;

/**
 * 
 * AES
 * Allows 16 byte key and 16 byte state, encodes into ASCii
 * AES(boolean crpyt, String inputStr, String key)
 * 
 *
 * @author Michael
 */
public class Encryption {

    public static void main(String[] args) {
        String key = "Thats my Kung Fu"; //has to be either 16, 24, or 32 in length
        String text = "Two One Nine Two";  //Two One Nine Two
        boolean crpt = true;
        
        AES a = new AES(crpt, text, key);
    }  
}
