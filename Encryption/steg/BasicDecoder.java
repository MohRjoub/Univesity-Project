package steg;

import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;

public class BasicDecoder implements Decoder {

	@Override
	public String decode(Image image) {
		if (image != null) {
			int width = (int) image.getWidth();
			int height = (int) image.getHeight();

			PixelReader reader = image.getPixelReader();

			List<int[]> validPixels = BasicEncoder.getInvolvedPixels(width, height);
			
			boolean[] bits = new boolean[validPixels.size()];

			for (int j = 0; j < bits.length; j++) {
				int[] newPixel = validPixels.get(j);
				int x = newPixel[0];
				int y = newPixel[1];
				int pixel = reader.getArgb(x,  y);
				String binary = Integer.toBinaryString(pixel);
				bits[j] = binary.charAt(binary.length() - 1) == '1';
			}

			// decode length
			int length = 0;
			for (int i = 0; i < 32; i++) {
				if (bits[i]) {
					length |= (1 << (31 - i));
				}
			}

			byte[] data = new byte[length];
			for (int i = 0; i < length; i++) {
				for (int j = 0; j < 8; j++) {
					if (bits[32 + i * 8 + j]) {
						data[i] |= (1 << (7 - j));
					}
				}
			}

			return new String(data);
		}
		return null;
	}

}
