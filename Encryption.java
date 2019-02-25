package encryption;

/**
 * 
 * AES
 * Allows 16 byte key and 16 byte state, encodes into ASCii
 *
 * @author Michael
 */
public class Encryption {

    public static void main(String[] args) {
        String key = "Thats my Kung Fu"; //has to be either 16, 24, or 32 in length
        String text = "Two One Nine Two";
        String cipherText = ")ÃP_W ö@\"³×:";
        boolean crpt = true;
        
        AES aesE = new AES(crpt, text, key);
        crpt = false;
        AES aesd = new AES(crpt, cipherText, key);
        
    }
    
}
