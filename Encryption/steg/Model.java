package steg;

import javafx.scene.image.Image;

public class Model {

    private Encoder encoder;
    private Decoder decoder;

    public Model(Encoder encoder, Decoder decoder) {
        this.encoder = encoder;
        this.decoder = decoder;
    }

    public Image encode(Image image, String message) {
        return encoder.encode(image, message);
    }

    public String decode(Image image) {
        return decoder.decode(image);
    }
}