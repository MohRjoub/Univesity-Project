import java.util.HashMap;
import java.util.Map;

public class RailFence {

    private Node[] railFence;
    private Map<Integer, Character> punctuation;

    
    public String railFenceEncrypt(String plainText, int key) {
        punctuation = new HashMap<>();
        railFence = new Node[key];
        if (key < 2) {
            return plainText;
            
        }
        String plainWithoutPunctuation = "";
        int length = plainText.length();
        for (int i = 0; i < length; i++) {
            if (Character.isLetter(plainText.charAt(i))) {
                plainWithoutPunctuation += plainText.charAt(i);
            } else {
                punctuation.put(i, plainText.charAt(i));
            }
        }
        int plainLength = plainWithoutPunctuation.length();
        for (int i = 0; i < key; i++) {
            railFence[i] = new Node(new String[plainLength]);
        }

        int[] arr = new int[key];
        for (int i = 0; i < key; i++) {
            arr[i] = 2 * (key - i - 1) - 1;
        }

        arr[key-1] = arr[0];

        for (int i = 0; i < key; i++) {
            if (i == 0 || i == key -1) {                
                for (int j = i; j < plainLength; j+=arr[i] + 1) {
                    railFence[i].rail[j] = plainWithoutPunctuation.charAt(j) + "";
                }
            } else {
                boolean turn = true;
                for (int j = i; j < plainLength;) {
                    railFence[i].rail[j] = plainWithoutPunctuation.charAt(j) + "";
                    if (!turn) {
                        j += arr[key-i-1] + 1;
                        turn = true;
                    } else {
                        j += arr[i] + 1;
                        turn = false;
                    }
                }
            }
        }
        
        String cipherText = "";
        for (int i = 0; i < key; i++) {
            for (int j = 0; j < railFence[i].rail.length; j++) {
                if (railFence[i].rail[j] != null) {
                    cipherText += railFence[i].rail[j];
                }    
            }
            cipherText += " ";
        }
        return cipherText;
    }

    public String railFenceDecrypt(String cipherText) {
        int key = cipherText.split(" ").length;
        if (key < 2) {
            return cipherText;
        }
        cipherText = cipherText.replaceAll(" ", "");
        railFence = new Node[key];
        int length = cipherText.length();
        for (int i = 0; i < key; i++) {
            railFence[i] = new Node(new String[length]);
        }

        int[] arr = new int[key];
        for (int i = 0; i < key; i++) {
            arr[i] = 2 * (key - i - 1) - 1;
        }

        arr[key-1] = arr[0];

        int index = 0;
        for (int i = 0; i < key; i++) {
            if (i == 0 || i == key -1) {                
                for (int j = i; j < length; j+=arr[i] + 1) {
                    railFence[i].rail[j] = cipherText.charAt(index++) + "";
                }
            } else {
                boolean turn = true;
                for (int j = i; j < length;) {
                    railFence[i].rail[j] = cipherText.charAt(index++) + "";
                    if (!turn) {
                        j += arr[key-i-1] + 1;
                        turn = true;
                    } else {
                        j += arr[i] + 1;
                        turn = false;
                    }
                }
            }
        }


        int plainTextLength = cipherText.length() + punctuation.size();
        String plainText = "";

        boolean turn = true;
        for (int i = 0, j = 0; j < length; j++, i=turn?i+1:i-1) {
            plainText += railFence[i].rail[j];
            if (i == key - 1) {
                turn = false;
            }
            if (!turn && i == 0) {
                turn = true;
            }
        }

        for (int i = 0; i < plainTextLength; i++) {
            if (punctuation.containsKey(i)) {
                plainText = plainText.substring(0, i) + punctuation.get(i) + plainText.substring(i);
            }
        }

        return plainText;
    }

    
    public Map<Integer, Character> getPunctuation() {
        return punctuation;
    }
    
    public Node[] getRailFence() {
        return railFence;
    }
    
    public class Node {
        private String[] rail;

        public Node(String[] rail) {
            this.rail = rail;
        }

        public String[] getRail() {
            return rail;
        }
    }
}
