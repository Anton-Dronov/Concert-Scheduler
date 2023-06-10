package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Engagement {
	private StringProperty Time;
	private StringProperty Status;
	private StringProperty Price_Reason;
	public Engagement(String time, String status, String price_Reason) {
		super();
		Time = new SimpleStringProperty(time);
		Status = new SimpleStringProperty(status); 
		Price_Reason = new SimpleStringProperty(price_Reason);
	}
	public String getTime() {
		return Time.get();
	}
	public String getStatus() {
		return Status.get();
	}
	public String getPrice_Reason() {
		return Price_Reason.get();
	}
	public StringProperty Price_ReasonProperty() {
		return Price_Reason;
	}
	public StringProperty TimeProperty() {
		return Time;
	}
	public StringProperty StatusProperty() {
		return Status;
	}
	public void setTime(String time) {
		Time = new SimpleStringProperty(time);
	}
	public void setStatus(String status) {
		Status = new SimpleStringProperty(status);
	}
	public void setPrice_Reason(String price_reason) {
		Price_Reason = new SimpleStringProperty(price_reason);
	}
}
