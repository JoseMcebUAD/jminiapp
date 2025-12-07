package com.jminiapp.examples.ceasar;

/**
 *
 * class used for transforming and comparing the cipher from teh user
 */
public class CeasarCipherConverter {

    /**
     * Ciphers the original text 
     * @param originalText text from the txt.files
     * @return cihpered text
     */
    public String cipherText(String originalText,int jumpNumber){
        char[] preCipheredtext = originalText.toCharArray();
        StringBuilder  cipheredText = new StringBuilder();
        //adds the jumpNumber to the strings
        for(char c:preCipheredtext ){

            if(c >= 'A' && c<= 'Z'){
                c = (char) ((c-'A' + jumpNumber)% 26 +'A'); 
            }

            else if(c >= 'a' && c<= 'z'){
                c = (char) ((c- 'a' + jumpNumber)% 26 +'a');
            }
            cipheredText.append(c);
        }

        return cipheredText.toString();
    }
    /**
     * Deciphers the ceasar text to the original
     * @param cipheredText ceasar text 
     * @param jumpNumber number of letters you have to jump
     * @return
     */

    public String dechipherText(String cipheredText,int jumpNumber){
        char[] preOriginalText = cipheredText.toCharArray();
        StringBuilder  originalText = new StringBuilder();
        //adds the jumpNumber to the strings
        for(char c:preOriginalText ){

            if(c >= 'A' && c<= 'Z'){
                c = (char) ((c-'A' - jumpNumber + 26)% 26 +'A');
            }

            else if(c >= 'a' && c<= 'z'){
                c = (char) ((c- 'a' - jumpNumber + 26)% 26 +'a');
            }
            originalText.append(c);
        }

        return originalText.toString();
    }

    public String compareCipheredToOriginalTex(String originalText, String userCipheredText,int jumpNumber){
        String userDecipheredtext  = this.dechipherText(userCipheredText, jumpNumber);

        // Compare the entire strings instead of word by word to avoid issues with punctuation
        if(userDecipheredtext.equals(originalText)){
            return "CONGRATULATIONS, YOU HAVE PASSED THE CAESAR CIPHER";
        }

        // If not exact match, provide detailed feedback
        String[] userArrayText = userDecipheredtext.split("\\s+");
        String[] originalArrayText = originalText.split("\\s+");

        //specify the number of errors the loop must restrict before breaking, must be lower to 5
        int toleranceWords = 0;
        StringBuilder incorrectWords = new StringBuilder();

        if(userArrayText.length != originalArrayText.length){
            throw new IllegalArgumentException("Word count mismatch: expected " + originalArrayText.length + " words, but got " + userArrayText.length + " words");
        }

        //compare the words of each text
        for(int i=0;i<originalArrayText.length;i++){
            //throw error if a word is incorrect
            if(!userArrayText[i].equals(originalArrayText[i])){
                String incorrectWord = userCipheredText.split("\\s+")[i];
                incorrectWords.append("Word number ").append(i+1)
                              .append(": '").append(incorrectWord)
                              .append("' is not correct (expected: '")
                              .append(this.cipherText(originalArrayText[i], jumpNumber))
                              .append("'). ");
                toleranceWords++;
            }

            if(toleranceWords > 5){
                throw new IllegalArgumentException("The text has more than 5 incorrect words, please check again or write it once more");
            }
        }
        //check if the user has a error
        if(incorrectWords.length() > 0){
            throw new IllegalArgumentException("The input is not correct: " + incorrectWords.toString());
        }

        return "CONGRATULATIONS, YOU HAVE PASSED THE CAESAR CIPHER";

    }
}
