import java.util.Random;

public class OneTimePad {
    private Random random;
    private byte[] cipherText;
    private byte[] key;

    public OneTimePad() {
        random = new Random();
    }

    public byte[] oneTimePadEncrypt(byte[] key, byte[] plainText, int length) {
        if (key.length < length) {
            throw new IllegalArgumentException("Key length is less than the specified length of plaintext");
        }
    
        byte[] cipherText = new byte[length];
        for (int i = 0; i < length; i++) {
            // XOR the plaintext with the key
            cipherText[i] = (byte) (plainText[i] ^ key[i]);
        }
        return cipherText;
    }
    
    public byte[] oneTimePadDecrypt(byte[] key, byte[] cipherText, int length) {
        if (key.length < length) {
            throw new IllegalArgumentException("Key length is less than the specified length of ciphertext");
        }
    
        byte[] decryptedText = new byte[length];
        for (int i = 0; i < length; i++) {
            // XOR the ciphertext with the key
            decryptedText[i] = (byte) (cipherText[i] ^ key[i]);
        }
        return decryptedText;
    }
 
    public byte[] oneTimePadEncryptBinary(byte[] key, byte[] plainText, int length) {
        if (key.length < plainText.length) {
            throw new IllegalArgumentException("Key length is less than the plaintext length");            
        }
        for (int i = 0; i < length; i++) {
            // XOR the message with the key
            plainText[i] ^= key[i];
        }
        return plainText;
    }

    public byte[] oneTimePadDecryptBinary(byte[] key, byte[] ciphertext, int length) {
        if (key.length < ciphertext.length) {
            throw new IllegalArgumentException("Key length is less than the ciphertext length");
        }
        for (int i = 0; i < length; i++) {
            // XOR the message with the key
            ciphertext[i] ^= key[i];
        }
        return ciphertext;
    }

    public byte[] generateKey(int length) {
        byte[] key = new byte[length];
        for (int i = 0; i < length; i++) {
            // Generate a random byte
            key[i] = (byte) random.nextInt();
        }
        return key;
    }

    public byte[] getKey() {
        return key;
    }

    public void setKey(byte[] key) {
        this.key = key;
    }

    public byte[] getCipherText() {
        return cipherText;
    }

    public void setCipherText(byte[] cipherText) {
        this.cipherText = cipherText;
    }

}