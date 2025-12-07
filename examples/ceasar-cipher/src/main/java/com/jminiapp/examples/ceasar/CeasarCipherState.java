package com.jminiapp.examples.ceasar;

/**
 * Model representing the state of a Caesar cipher challenge.
 *
 * <p>This class encapsulates both the data (original text, jump number, user input)
 * and the behavior (cipher, decipher, compare) for a Caesar cipher challenge.</p>
 *
 * <p>This class uses composition and delegates cipher operations to CeasarCipherConverter.</p>
 */
public class CeasarCipherState {
    private String originalText;
    private int jumpNumber;
    private String userCipheredText;
    private boolean completed = false;
    private final CeasarCipherConverter converter = new CeasarCipherConverter();

    /**
     * Create a new Caesar cipher state with default values.
     */
    public CeasarCipherState() {
        this.jumpNumber = 3;
        this.completed = false;
    }

    /**
     *
     * @param originalText the original text to be ciphered
     * @param jumpNumber the number of positions to shift in the alphabet
     */
    public CeasarCipherState(String originalText, int jumpNumber) {
        this.originalText = originalText;
        this.jumpNumber = jumpNumber;
        this.userCipheredText = "";
    }

    // ==================== Getters and Setters ====================

    public String getOriginalText() {
        return originalText;
    }

    public void setOriginalText(String originalText) {
        this.originalText = originalText;
    }

    public int getJumpNumber() {
        return jumpNumber;
    }

    public void setJumpNumber(int jumpNumber) {
        this.jumpNumber = jumpNumber;
    }

    public String getUserCipheredText() {
        return userCipheredText;
    }

    public void setUserCipheredText(String userCipheredText) {
        this.userCipheredText = userCipheredText;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    /**
     * Ciphers a text using the Caesar cipher algorithm.
     *
     * @param text the text to cipher
     * @param shift the number of positions to shift
     * @return the ciphered text
     */
    public String cipherText(String text, int shift) {
        return converter.cipherText(text, shift);
    }

    /**
     * Ciphers the original text stored in this state.
     *
     * @return the ciphered text
     */
    public String cipherText() {
        return converter.cipherText(this.originalText, this.jumpNumber);
    }

    /**
     * deciphers a Caesar cipher text to the original.     *
     * @param cipheredText the Caesar cipher text
     * @param shift the number of positions that were shifted
     * @return the deciphered text
     */
    public String decipherText(String cipheredText, int shift) {
        return converter.dechipherText(cipheredText, shift);
    }

    /**
     * deciphers the user's ciphered text stoeded in this state
     * @return the deciphered text
     */
    public String decipherUserText() {
        return converter.dechipherText(this.userCipheredText, this.jumpNumber);
    }

    /**
     * compares the user's ciphered text with te original text.
     *
     * @param originalText the original text
     * @param userCipheredText the user's ciphered text
     * @param shift the number of positions shifted
     * @return success message if the text is correct
     */
    public String compareCipheredToOriginalText(String originalText, String userCipheredText, int shift) {
        return converter.compareCipheredToOriginalTex(originalText, userCipheredText, shift);
    }

    /**
     * compares the user's ciphered text stored in this state with the original text.
     *
     * @return success message if the text is correct
     * @throws IllegalArgumentException if the text has errors
     */
    public String compareUserText() {
        String result = converter.compareCipheredToOriginalTex(this.originalText, this.userCipheredText, this.jumpNumber);
        this.completed = true;
        return result;
    }

    /**
     * resets the challenge to initial state.
     */
    public void reset() {
        this.userCipheredText = "";
        this.completed = false;
    }

    @Override
    public String toString() {
        return "CeasarCipherState{" +
                "originalText='" + originalText + '\'' +
                ", jumpNumber=" + jumpNumber +
                ", completed=" + completed +
                '}';
    }
}
