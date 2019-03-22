package encryption;

/**
 *
 * @author Michael
 */
public class Base64 {
    public String encodeBase64(String input){
        char[] base64 = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','y','x','z','0','1','2','3','4','5','6','7','8','9','+','/'};
        double inputLength = input.length();
        int inputLengthsub = (int)Math.ceil(inputLength/3.0);
        
        String[] inputArr = new String[inputLengthsub];
        String[] encodedArr = new String[inputLengthsub];
        
        for(int i = 0; i < inputLengthsub; i++){ 
            if(i*3+2 <= inputLength-1)
                inputArr[i]= input.substring(i*3,i*3+3);
            else{
                if(inputLength % 3 == 1)
                    inputArr[i] = input.substring(i*3,i*3+1);
                if(inputLength % 3 == 2)
                    inputArr[i] = input.substring(i*3,i*3+2);
            }
        }
        
        for(int z = 0; z < inputArr.length; z++){        
        
            //converts text to an array of ascii
            int[] texthex = new int[inputArr[z].length()];
            int count = 0;
            for(int i = 0; i < inputArr[z].length(); i++){ 
                char temp = inputArr[z].charAt(count);
                texthex[i] = temp; 
                count++;
            } 

            //Create array of bits
            char[] arrStr;
            if(texthex.length % 3 == 1)
                arrStr = new char[texthex.length *8 +4];
            else if(texthex.length % 3 == 2)
                arrStr = new char[texthex.length *8 +2];
            else
                arrStr = new char[texthex.length *8];
            
            //Fill the Array with 0s
            for(int j = 0; j < arrStr.length; j++)
                    arrStr[j] = '0';

            //Take each value of the int[] and convert it to binary
            for(int i = 0; i < texthex.length; i++){
                int dec = texthex[i];
                int x = i*8 + 7;
                while(dec > 0 ){
                    int mod = dec % 2;
                    dec = dec / 2;
                    arrStr[x] = (char)(mod+48);
                    x--;
                }
            }
            
            //Convert the binary back to an int, but using 6 bit instaed of 8 bits
            int dec;
            int[] sextetsBase64 = new int[texthex.length+1];
            String val = new String(arrStr);
            for(int i = 0; i < texthex.length + 1; i++){
                dec = 0;
                for(int j = 0; j < 6; j++){
                    //charAt(length - current - 1)
                    char temp = val.charAt((i*6+6) - j - 1);
                    int asc = temp;
                    if(asc >= 48 && asc <= 57)
                        dec =dec + ((asc - 48) * (int) Math.pow(2,j));
                }
                sextetsBase64[i] = dec;
            }

            //Take the int and using base64 array create a string
            StringBuilder finalsb = new StringBuilder();
            for(int k = 0; k < sextetsBase64.length; k++){
                finalsb.insert(k, base64[sextetsBase64[k]]);
            }
            if(texthex.length == 1){
                finalsb.insert(2, "==");
            }
            if(texthex.length == 2){
                finalsb.insert(3, "=");
            }
            encodedArr[z] = finalsb.toString();  
        }
        
        String finalstr = String.join("", encodedArr);
        return finalstr;
    }
    
    public String decodeBase64(String input){       
        double inputLength = input.length();
        int inputLengthsub = (int)Math.ceil(inputLength/4.0);
        
        String[] inputArr = new String[inputLengthsub];
        String[] decodedArr = new String[inputLengthsub];
        
        for(int i = 0; i < inputLengthsub; i++){ 
            if(i*4+2 <= inputLength-1)
                inputArr[i]= input.substring(i*4,i*4+4);
            else{
                if(inputLength % 4 == 2)
                    inputArr[i] = input.substring(i*4,i*4+2);
                if(inputLength % 4 == 3)
                    inputArr[i] = input.substring(i*4,i*4+3);
            }
        }
        
        for(int z = 0; z < inputArr.length; z++){ 
        
            //converts the input text to an array of ascii
            int count = 0;
            int[] base64hex = new int[inputArr[z].length()];
            for(int i = 0; i < inputArr[z].length(); i++){ //loop through rows
                char temp = inputArr[z].charAt(count);
                base64hex[i] = temp; 
                count++;
            }

            //converting the ascii value to base64
            int[] sextetsBase64 = new int[base64hex.length];
            for(int i = 0; i < base64hex.length; i++){
                int asc = base64hex[i];
                if(asc >= 48 && asc <= 57)  //0 - 9
                    sextetsBase64[i] = asc + 4;
                else if(asc >= 65 && asc <= 90) //uppercase
                    sextetsBase64[i] = asc - 65;
                else if(asc >= 97 && asc <= 122) //lower case
                    sextetsBase64[i] = asc - 71;
                else if(asc == 43) // +
                    sextetsBase64[i] = 62;
                else if(asc == 47) // /
                    sextetsBase64[i] = 63;
                else if(asc == 61) // =
                    sextetsBase64[i] = 100;
            }

            //convert to binary
            char[] binStr;
            int length = sextetsBase64.length;

            if(sextetsBase64[2] == 100){
                binStr = new char[12];
                length--;
                length--;
            } else if(sextetsBase64[3] == 100){
                binStr = new char[18];
                length--;
            } else{
                binStr = new char[24];
            }

            for(int j = 0; j < binStr.length; j++)
                    binStr[j] = '0';

            for(int i = 0; i < length; i++){
                int dec = sextetsBase64[i];
                int x = i*6 + 5;
                while(dec > 0){
                    int mod = dec % 2;
                    dec = dec / 2;
                    binStr[x] = (char)(mod+48);
                    x--;
                }
            }

            //convert back to ascii but with 8 bits instead of 6
           int[] asc = new int[binStr.length / 8];
            for(int i = 0; i < binStr.length / 8; i++){
                int total = 0;
                int counter = 0;
                for(int j = (i+1)*8-1; j >= i*8; j--){
                    total = total + (binStr[j] - 48) * (int) Math.pow(2, counter);
                    counter++;
                }
                asc[i] = total;
            }

            StringBuilder str = new StringBuilder();

            for(int i = 0; i < asc.length; i++)
                str.insert(i, (char) asc[i]);

            decodedArr[z] = str.toString();   
        }
        String finalstr = String.join("", decodedArr);
        return finalstr;
    }
    
    
    
    
    
    public String encodeBase64Arry(int[] input){
        char[] base64 = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','y','x','z','0','1','2','3','4','5','6','7','8','9','+','/'};
        double inputLength = input.length;
        int inputLengthsub = (int)Math.ceil(inputLength/3.0);
        
        String[] encodedArr = new String[inputLengthsub];

        for(int z = 0; z < inputLengthsub; z++){        
            int[] texthex;
            
            if(z*3+3<=input.length){
                texthex = new int[3];
                texthex[0] = input[z*3+0];
                texthex[1] = input[z*3+1];
                texthex[2] = input[z*3+2];
            }else{
                if(input.length % 3 == 2){
                    texthex = new int[2];
                    texthex[0] = input[z*3+0];
                    texthex[1] = input[z*3+1];
                }else if(input.length % 3 == 1){
                    texthex = new int[1];
                    texthex[0] = input[z*3+0];
                }else
                    texthex = new int[0];
            }
            
            
            int[] bits;
            if(texthex.length == 1)
                bits = new int[texthex.length *8 +4];
            else if(texthex.length == 2)
                bits = new int[texthex.length *8 +2];
            else
                bits = new int[texthex.length *8];

            for(int j = 0; j < bits.length; j++)
                    bits[j] = 0;


            for(int i = 0; i < texthex.length; i++){
                int dec = texthex[i];
                int x = i*8 + 7;
                while(dec > 0){
                    int mod = dec % 2;
                    dec = dec / 2;
                    bits[x] = mod;
                    x--;
                }
            }
            int dec;
            int[] sextetsBase64 = new int[texthex.length+1];

            for(int i = 0; i < texthex.length + 1; i++){
                dec = 0;
                for(int j = 0; j < 6; j++){
                    //charAt(length - current - 1)
                    dec = dec + (bits[(i*6+6) - j - 1] * (int) Math.pow(2,j));
                }
                sextetsBase64[i] = dec;
            }


            StringBuilder finalsb = new StringBuilder();
            for(int k = 0; k < sextetsBase64.length; k++){
                finalsb.insert(k, base64[sextetsBase64[k]]);
            }
            if(texthex.length == 1){
                finalsb.insert(2, "==");
            }
            if(texthex.length == 2){
                finalsb.insert(3, "=");
            }
            encodedArr[z] = finalsb.toString();  
        }
        
        String finalstr = String.join("", encodedArr);
        return finalstr;
    }
    
    //decode input String output array of ascii
    public int[] decodeBase64Arry(String input){       
        double inputLength = input.length();
        int inputLengthsub = (int)Math.ceil(inputLength/4.0);
        
        String[] inputArr = new String[inputLengthsub];
        int[] decodedArr = new int[inputLengthsub*3];  //change size   minus padding
        
        for(int i = 0; i < inputLengthsub; i++){ 
            if(i*4+2 <= inputLength-1)
                inputArr[i]= input.substring(i*4,i*4+4);
            else{
                if(inputLength % 4 == 2)
                    inputArr[i] = input.substring(i*4,i*4+2);
                if(inputLength % 4 == 3)
                    inputArr[i] = input.substring(i*4,i*4+3);
            }
        }
        
        for(int z = 0; z < inputArr.length; z++){ 
        
            //converts the input text to an array of ascii
            int count = 0;
            int[] base64hex = new int[inputArr[z].length()];
            for(int i = 0; i < inputArr[z].length(); i++){ //get ride of count replace with i
                char temp = inputArr[z].charAt(count);
                base64hex[i] = temp; 
                count++;
            }

            int[] sextetsBase64 = new int[base64hex.length];
            for(int i = 0; i < base64hex.length; i++){
                int asc = base64hex[i];
                if(asc >= 48 && asc <= 57)  //0 - 9
                    sextetsBase64[i] = asc + 4;
                else if(asc >= 65 && asc <= 90) //uppercase
                    sextetsBase64[i] = asc - 65;
                else if(asc >= 97 && asc <= 122) //lower case
                    sextetsBase64[i] = asc - 71;
                else if(asc == 43) // +
                    sextetsBase64[i] = 62;
                else if(asc == 47) // /
                    sextetsBase64[i] = 63;
                else if(asc == 61) // =
                    sextetsBase64[i] = 100;
            }

            //convert to binary
            int[] bits;

            if(sextetsBase64[2] == 100)
                bits = new int[12];
            else if(sextetsBase64[3] == 100)
                bits = new int[18];
            else
                bits = new int[24];
            

            for(int j = 0; j < bits.length; j++)
                    bits[j] = 0;

            for(int i = 0; i < bits.length / 6; i++){
                int dec = sextetsBase64[i];
                int x = i*6 + 5;
                while(dec > 0){
                    int mod = dec % 2;
                    dec = dec / 2;
                    bits[x] = mod;
                    x--;
                }
            }

            //convert back to ascii but with 8 bits instead of 6
           int[] asc = new int[bits.length / 8];
            for(int i = 0; i < bits.length / 8; i++){
                int total = 0;
                int counter = 0;
                for(int j = (i+1)*8-1; j >= i*8; j--){
                    total = total + (bits[j]) * (int) Math.pow(2, counter);
                    counter++;
                }
                asc[i] = total;
            }
            for(int i = 0; i < asc.length; i++){ 
                decodedArr[z*3+i] = asc[i]; 
            }    
        }
        return decodedArr;
    }
    
    
}
