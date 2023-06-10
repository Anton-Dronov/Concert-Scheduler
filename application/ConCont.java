package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

public class ConCont implements Initializable{
	@FXML
	private DatePicker WhatDate;
	@SuppressWarnings("rawtypes")
	@FXML
	private Spinner WhatTime;
	@FXML
	private TextField Reason;
	@FXML
	private Label Regst;
	@FXML
	private Label whatAbout;
	@FXML
	private TableView<Engagement> TW0, TW1, TW2, TW3, TW4, TW5, TW6, TW7, TW8, TW9, TW10, TW11, TW12, TW13, TW14, TW15, TW16, TW17, TW18, TW19, TW20, TW21, TW22, TW23, TW24, TW25, TW26, TW27, TW28, TW29;
	@FXML
	private TableColumn<Engagement, String> time1, status1, reason1, time2, status2, reason2, time3, status3, reason3, time4, status4, reason4, time5, status5, reason5, time6, status6, reason6, time7, status7, reason7, time8, status8, reason8, time9, status9, reason9, time10, status10, reason10, time11, status11, reason11, time12, status12, reason12, time13, status13, reason13, time14, status14, reason14, time15, status15, reason15, time16, status16, reason16, time17, status17, reason17, time18, status18, reason18, time19, status19, reason19, time20, status20, reason20, time21, status21, reason21, time22, status22, reason22, time23, status23, reason23, time24, status24, reason24, time25, status25, reason25, time26, status26, reason26, time27, status27, reason27, time28, status28, reason28, time29, status29, reason29, time0, status0, reason0;
	
	public Statement Connect() throws SQLException { // connecting to the database
		Statement stmt = DriverManager.getConnection("jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=Concert_Scheduler;integratedSecurity=true;encrypt=false").createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		return stmt;
	}
	
	@FXML
	public void Proceed(ActionEvent event){
		
		/* Input information */
		String reason = Reason.getText(); // Why you need the concert hall?
		try {
			int hour = (int) WhatTime.getValue(); // What time of the day?
			String Time = hour + ":00"; // giving an appropriate format for the time
			LocalDate date = WhatDate.getValue(); // On which date you want?
			
			if (date == null) { // If the date wasn't given
				whatAbout.setText("Insert the date!");
			} else {
			
				/* Adding occupation into the database */
				if (!(CurrentUser.getUsername() == null)) { // Is user has registered?
					try {
						Connect().execute("INSERT INTO Occupation VALUES (CONVERT(TIME, '" + Time + "'), CONVERT(DATE, '" + date + "'), '" + reason + "', '" + CurrentUser.getUsername() + "');"); // Inserting an information into the database
						ChangeTable(date, Time, reason); // Changing the information in an appropriate table
						whatAbout.setText("Succesfully occupied!"); // Alerting user
					} catch (SQLException S) { // If the time to occupate has been already busy
						ResultSet checkCondition;
						try {
							checkCondition = Connect().executeQuery("SELECT ByWho FROM Occupation WHERE Tstp = CONVERT(TIME, '" + Time + "') AND Kog = CONVERT(DATE, '" + date + "');"); // Receiving information about user
							String prover = "";
							while (checkCondition.next()) {
								prover = checkCondition.getString("ByWho"); // Who is time consumer?
							}
							if (prover.equals(CurrentUser.getUsername())) {
								Connect().execute("UPDATE Occupation SET Reason = '" + reason + "' WHERE Tstp = CONVERT(TIME, '" + Time + "') AND Kog = CONVERT(DATE, '" + date + "');"); // Updating an information about busy time reason
								whatAbout.setText("The reason changed!");
								ChangeTable(date, Time, reason); // Changing the information in an appropriate table
							} else {
								whatAbout.setText("This time is occupied!");
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				} else { // If user has not been logged in
					whatAbout.setText("Login firstly!");
				}
			}
		} catch (NullPointerException NPE) { // case when Time value is zero
			whatAbout.setText("Enter the time!");
		}
	}

	@FXML
	public void Delete(ActionEvent event) {
		try {
			int hour = (int) WhatTime.getValue(); // What time of the day?
			String Time = hour + ":00"; // giving an appropriate format for the time
			LocalDate date = WhatDate.getValue(); // On which date you want?
			if (date == null) { // If the date wasn't given
				whatAbout.setText("Insert the date!");
			} else {
				if (!(CurrentUser.getUsername() == null)) { // Is user has registered?
					ResultSet checkCondition;
					try {
						checkCondition = Connect().executeQuery("SELECT ByWho FROM Occupation WHERE Tstp = CONVERT(TIME, '" + Time + "') AND Kog = CONVERT(DATE, '" + date + "');"); // Receiving information about user
						String prover = "";
						while (checkCondition.next()) {
							prover = checkCondition.getString("ByWho"); // Who is time consumer?
						}
						if (prover.equals(CurrentUser.getUsername())) {
							Connect().execute("DELETE Occupation WHERE Tstp = CONVERT(TIME, '" + Time + "') AND Kog = CONVERT(DATE, '" + date + "');"); // time slot clearing
							ChangeTable(date, Time, "DeleteThisTimeStamp"); // updating appropriate table
							whatAbout.setText("Succesfully deleted!"); // Alerting user
						} else {
							whatAbout.setText("You are not allowed!"); // Alerting user
						}
					} catch (SQLException S) {
						S.printStackTrace();
					}
				} else { // If user has not been logged in
					whatAbout.setText("Login firstly!");
				}
			}
		} catch (NullPointerException NPE) { // case when Time value is zero
			whatAbout.setText("Enter the time!");
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		/* Filling every table of the June 2023, with the time, status and price values */
		fillWorkTable(TW0, time0, status0, reason0);
		fillWorkTable(TW1, time1, status1, reason1);
		fillWeekendTable(TW2, time2, status2, reason2);
		fillWeekendTable(TW3, time3, status3, reason3);
		fillWorkTable(TW4, time4, status4, reason4);
		fillWorkTable(TW5, time5, status5, reason5);
		fillWorkTable(TW6, time6, status6, reason6);
		fillWorkTable(TW7, time7, status7, reason7);
		fillWorkTable(TW8, time8, status8, reason8);
		fillWeekendTable(TW9, time9, status9, reason9);
		fillWeekendTable(TW10, time10, status10, reason10);
		fillWorkTable(TW11, time11, status11, reason11);
		fillWorkTable(TW12, time12, status12, reason12);
		fillWorkTable(TW13, time13, status13, reason13);
		fillWorkTable(TW14, time14, status14, reason14);
		fillWorkTable(TW15, time15, status15, reason15);
		fillWeekendTable(TW16, time16, status16, reason16);
		fillWeekendTable(TW17, time17, status17, reason17);
		fillWorkTable(TW18, time18, status18, reason18);
		fillWorkTable(TW19, time19, status19, reason19);
		fillWorkTable(TW20, time20, status20, reason20);
		fillWorkTable(TW21, time21, status21, reason21);
		fillWorkTable(TW22, time22, status22, reason22);
		fillWeekendTable(TW23, time23, status23, reason23);
		fillWeekendTable(TW24, time24, status24, reason24);
		fillWorkTable(TW25, time25, status25, reason25);
		fillWorkTable(TW26, time26, status26, reason26);
		fillWorkTable(TW27, time27, status27, reason27);
		fillWorkTable(TW28, time28, status28, reason28);
		fillWorkTable(TW29, time29, status29, reason29);
		yourAccount(); // To show information about the registration
		try {
			occupTable(); // Changing information in tables according to the information from the database
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/* Filling tables of workdays */
	private void fillWorkTable(TableView<Engagement> TW, TableColumn<Engagement, String> TC1, TableColumn<Engagement, String> TC2, TableColumn<Engagement, String> TC3) {
		ObservableList<Engagement> data_workday = FXCollections.observableArrayList( // data to be stored in every workday table
				new Engagement ("08:00", "free", "10.00"),
				new Engagement ("09:00", "free", "15.00"),
				new Engagement ("10:00", "free", "18.00"),
				new Engagement ("11:00", "free", "18.00"),
				new Engagement ("12:00", "free", "18.00"),
				new Engagement ("13:00", "free", "20.00"),
				new Engagement ("14:00", "free", "20.00"),
				new Engagement ("15:00", "free", "25.00"),
				new Engagement ("16:00", "free", "25.00"),
				new Engagement ("17:00", "free", "25.00"),
				new Engagement ("18:00", "free", "25.00"),
				new Engagement ("19:00", "free", "20.00"),
				new Engagement ("20:00", "free", "18.00"),
				new Engagement ("21:00", "free", "15.00"),
				new Engagement ("22:00", "free", "10.00")
		);
		TW.setItems(data_workday);
	    TC1.setCellValueFactory(data -> data.getValue().TimeProperty());
	    TC2.setCellValueFactory(data -> data.getValue().StatusProperty());
	    TC3.setCellValueFactory(data -> data.getValue().Price_ReasonProperty());
	}
	
	/* Filling tables of weekends */
	private void fillWeekendTable(TableView<Engagement> TW, TableColumn<Engagement, String> TC1, TableColumn<Engagement, String> TC2, TableColumn<Engagement, String> TC3) {
		ObservableList<Engagement> data_weekend = FXCollections.observableArrayList( // data to be stored in every weekend table
				new Engagement ("08:00", "free", "12.00"),
				new Engagement ("09:00", "free", "15.00"),
				new Engagement ("10:00", "free", "20.00"),
				new Engagement ("11:00", "free", "20.00"),
				new Engagement ("12:00", "free", "20.00"),
				new Engagement ("13:00", "free", "22.00"),
				new Engagement ("14:00", "free", "22.00"),
				new Engagement ("15:00", "free", "27.00"),
				new Engagement ("16:00", "free", "30.00"),
				new Engagement ("17:00", "free", "30.00"),
				new Engagement ("18:00", "free", "27.00"),
				new Engagement ("19:00", "free", "25.00"),
				new Engagement ("20:00", "free", "20.00"),
				new Engagement ("21:00", "free", "15.00"),
				new Engagement ("22:00", "free", "12.00")
		);
		TW.setItems(data_weekend);
	    TC1.setCellValueFactory(data -> data.getValue().TimeProperty());
	    TC2.setCellValueFactory(data -> data.getValue().StatusProperty());
	    TC3.setCellValueFactory(data -> data.getValue().Price_ReasonProperty());
	}
	
	@FXML
	public void LogIn(ActionEvent event){ // What happens when we press the "Login" button?
		try {
			/* Opening the "Login" stage */
			Stage stage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			Parent root = loader.load(getClass().getResource("Registration.fxml").openStream());
			stage.setScene(new Scene(root));
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void SignUP(ActionEvent event){ // What happens when we press the "Register" button?
		try {
			/* Opening the "Registration" stage */
			Stage stage = new Stage();
			FXMLLoader loader = new FXMLLoader();
			Parent root = loader.load(getClass().getResource("Rega.fxml").openStream());
			stage.setScene(new Scene(root));
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/* Changing an information in the table */
	public void ChangeTable(LocalDate date, String time, String reason) { 
		TableView<Engagement> Tabl; // Which table will be changed?
		int row; // Which row in this table will be changed?
		String pattern = "dd/MM/yyyy"; // date pattern
		DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
		String dated = df.format(date); // Formatting the date to the pattern above
		Tabl = switch (dated) { // Changing table dependent from the date
			case "01/06/2023" -> TW0;
			case "02/06/2023" -> TW1;
			case "03/06/2023" -> TW2;
			case "04/06/2023" -> TW3;
			case "05/06/2023" -> TW4;
			case "06/06/2023" -> TW5;
			case "07/06/2023" -> TW6;
			case "08/06/2023" -> TW7;
			case "09/06/2023" -> TW8;
			case "10/06/2023" -> TW9;
			case "11/06/2023" -> TW10;
			case "12/06/2023" -> TW11;
			case "13/06/2023" -> TW12;
			case "14/06/2023" -> TW13;
			case "15/06/2023" -> TW14;
			case "16/06/2023" -> TW15;
			case "17/06/2023" -> TW16;
			case "18/06/2023" -> TW17;
			case "19/06/2023" -> TW18;
			case "20/06/2023" -> TW19;
			case "21/06/2023" -> TW20;
			case "22/06/2023" -> TW21;
			case "23/06/2023" -> TW22;
			case "24/06/2023" -> TW23;
			case "25/06/2023" -> TW24;
			case "26/06/2023" -> TW25;
			case "27/06/2023" -> TW26;
			case "28/06/2023" -> TW27;
			case "29/06/2023" -> TW28;
			case "30/06/2023" -> TW29;
			default -> null;
		};
		row = switch (time) { // Changing row dependent from the timestamp
			case "8:00", "08:00" -> 0;
			case "9:00", "09:00" -> 1;
			case "10:00" -> 2;
			case "11:00" -> 3;
			case "12:00" -> 4;
			case "13:00" -> 5;
			case "14:00" -> 6;
			case "15:00" -> 7;
			case "16:00" -> 8;
			case "17:00" -> 9;
			case "18:00" -> 10;
			case "19:00" -> 11;
			case "20:00" -> 12;
			case "21:00" -> 13;
			case "22:00" -> 14;
			default -> -1;
		};
		Engagement whatSet;
		if (reason.equals("DeleteThisTimeStamp")) { // somebody wants to delete the occupation
			String Price;
			if (Tabl == TW2 || Tabl == TW3 || Tabl == TW9 || Tabl == TW10 || Tabl == TW16 || Tabl == TW17 || Tabl == TW23 || Tabl == TW24) { // if it is weekend
				Price = switch (time) { // choosing the price depending on the time
					case "8:00", "08:00", "22:00" -> "12.00";
					case "9:00", "09:00", "21:00" -> "15.00";
					case "10:00", "11:00", "12:00", "20:00" -> "20.00";
					case "13:00", "14:00" -> "22.00";
					case "15:00", "18:00" -> "27.00";
					case "16:00", "17:00" -> "30.00";
					case "19:00" -> "25.00";
					default -> "0.00";
				};
			} else {
				Price = switch (time) {
				case "8:00", "08:00", "22:00" -> "10.00";
				case "9:00", "09:00", "21:00" -> "15.00";
				case "10:00", "11:00", "12:00", "20:00" -> "18.00";
				case "13:00", "14:00", "19:00" -> "20.00";
				case "15:00", "18:00", "16:00", "17:00" -> "25.00";
				default -> "0.00";
			};
			}
			whatSet = new Engagement(time, "free", Price); // Deleting the occupation
		} else {
			whatSet = new Engagement(time, "busy", reason); // New occupation to show
		}
		if (Tabl != null) Tabl.getItems().set(row, whatSet); // Filling only existing tables
	}
	
	/* Selecting all occupations from the database */
	public void occupTable() throws SQLException {
		ResultSet busyTime = Connect().executeQuery("SELECT Tstp, Kog, Reason FROM Occupation;"); // Receiving an information from the database
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
		while (busyTime.next()) {
			LocalTime times = busyTime.getTime("Tstp").toLocalTime();
			String time = times.format(dtf); // converting from LocalTime type to String type
			ChangeTable(busyTime.getDate("Kog").toLocalDate(), time, busyTime.getString("Reason")); // Changing an appropriate table and row
		}
	}
	
	/* Showing which account is registered now */
	public void yourAccount() {
		if (CurrentUser.getUsername() == null) { // Is the user yet not logged in?
			Regst.setText("Login to an account!");
		} else {
			Regst.setText(CurrentUser.getUsername());
		}
	}
}