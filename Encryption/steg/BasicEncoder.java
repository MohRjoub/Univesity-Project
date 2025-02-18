package steg;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class BasicEncoder implements Encoder {

	@Override
	public Image encode(Image image, String messgae) {
		int width = (int) image.getWidth();
		int height = (int) image.getHeight();

		WritableImage copy = new WritableImage(image.getPixelReader(), width, height);
		PixelWriter writer = copy.getPixelWriter();
		PixelReader reader = image.getPixelReader();

		boolean[] bits = encode(messgae);

		List<int[]> validPixels = getInvolvedPixels(width, height);

		if (validPixels.size() < bits.length) {
			System.out.println("Not enough space to store the message");
		} else {
			for (int i = 0; i < bits.length; i++) {
				int[] newPixel = validPixels.get(i);
				int x = newPixel[0];
				int y = newPixel[1];
				int pixel = reader.getArgb(x, y);
				writer.setArgb(x, y, bits[i] ? pixel | 1 : pixel & ~1);
			}
			System.out.println("Done Encoding");
		}

		return copy;
	}

	private boolean[] encode(String message) {
		byte[] data = message.getBytes();

		// int = 32 bits
		// byte = 8 bits
		boolean[] bits = new boolean[32 + data.length * 8];

		// encoded length
		String binary = Integer.toBinaryString(data.length);
		while (binary.length() < 32) {
			binary = "0" + binary;
		}

		for (int i = 0; i < 32; i++) {
			bits[i] = binary.charAt(i) == '1';
		}

		// [7, 6, 5 ... 0]
		// encode message
		for (int i = 0; i < data.length; i++) {
			byte b = data[i];

			for (int j = 0; j < 8; j++) {
				bits[32 + i * 8 + j] = (b >> (7 - j) & 1) == 1;
			}
		}

		return bits;
	}

	public static List<int[]> getInvolvedPixels(int width, int height) {
		List<int[]> diagonalPixels = new ArrayList<>();

        for (int startY = 0; startY < height; startY += 2) {
            for (int startX = 0; startX < width; startX += 2) {
                
                // Check if we are inside the current block
                if (startX < width && startY < height) {
                    diagonalPixels.add(new int[]{startX, startY}); // first pixel in the diagonal
                }
                if (startX + 1 < width && startY + 1 < height) {
                    diagonalPixels.add(new int[]{startX + 1, startY + 1}); // second pixel in the diagonal
                }
            }
        }
        
        return diagonalPixels;
	}
}
