package steg;

import javafx.scene.image.Image;

public interface Encoder {
	Image encode(Image image, String messgae);
}
