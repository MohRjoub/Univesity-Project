package application;

import javafx.beans.property.*;

// class to hold frequency table data
public class Frequency implements Comparable<Frequency>{
    private final SimpleObjectProperty<Character> character;
    private final SimpleIntegerProperty frequency;
    private final SimpleStringProperty code;
    private final SimpleIntegerProperty size;

    public Frequency(char character, int frequency, String code, int size) {
        this.character = new SimpleObjectProperty<>(character);
        this.frequency = new SimpleIntegerProperty(frequency);
		this.code = new SimpleStringProperty(code);
		this.size = new SimpleIntegerProperty(size);
    }

    public SimpleObjectProperty<Character> characterProperty() {
        return character;
    }

    public SimpleIntegerProperty frequencyProperty() {
        return frequency;
    }
    
    public SimpleStringProperty codeProperty() {
        return code;
    }
    
    public SimpleIntegerProperty sizeProperty() {
        return size;
    }

	@Override
	public int compareTo(Frequency o) {
		return this.frequency.get() - o.frequency.get();
	}
}

