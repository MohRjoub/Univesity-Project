package Controller;

import javafx.beans.property.IntegerProperty;
// data type to hold dp table integers
public class RowData {
    private IntegerProperty[] dpRowValues;
    private String coin;

    public RowData(IntegerProperty[] dpRowValues, String coin) {
        this.dpRowValues = dpRowValues;
        this.coin = coin;
    }

    public IntegerProperty getValueProperty(int index) {
        return dpRowValues[index];
    }

	public String getCoin() {
		return coin;
	}

	public void setCoin(String coin) {
		this.coin = coin;
	}
    
    
}

