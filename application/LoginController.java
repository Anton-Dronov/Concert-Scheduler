package application;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
	@FXML
	private Button Butt;
	@FXML
	private TextField LogName;
	@FXML
	private PasswordField PassCode;
	@FXML
	private Label MainLog;

	public Statement Connect() throws SQLException { // connecting to the database
		Statement stmt = DriverManager.getConnection("jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=Concert_Scheduler;integratedSecurity=true;encrypt=false").createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		return stmt;
	}
	
	@FXML
	public void Login_Now(ActionEvent Event) throws NoSuchAlgorithmException, SQLException, IOException {
		ArrayList<User> regUsers = new ArrayList<User>(); // list of all registered users
		String login = LogName.getText(); // getting username from the user
		String password = PassCode.getText(); // getting the password from the user
		
		/* Ciphering it by the MD5 algorithm */
        MessageDigest md = MessageDigest.getInstance("MD5"); 
        byte[] messageDigest = md.digest(password.getBytes());
        BigInteger no = new BigInteger(1, messageDigest);
        String hashtext = no.toString(16);
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        
        ResultSet AllLogs = Connect().executeQuery("SELECT Username, [Password] FROM Client;"); // getting all registered users from the database
        while(AllLogs.next()) {
        	User u = new User(AllLogs.getString("Username"), AllLogs.getString("Password")); // Creating a new User object
        	regUsers.add(u); // Adding it into the ArrayList
        }
        
        Boolean user_flag = false; // is username exists in the database?
        Boolean password_flag = false; // is password is correct?
        for (int i = 0; i < regUsers.size(); i++) { // Checking for the registered user
        	if (regUsers.get(i).getUsername().equals(login)) {
        		user_flag = true; // The user has been found
        		if (regUsers.get(i).getPassword().equals(hashtext)) {
        			password_flag = true; // The password is correct
        			new CurrentUser(regUsers.get(i).getUsername(), regUsers.get(i).getPassword()); // Login the user into the app
        			break;
        		} else break;
        	}
        }
        
        if (user_flag == false) { // The user with the given username was not found case
        	MainLog.setText("No such user in a database!");
        } else if (password_flag == false) { // The password is incorrect case
        	MainLog.setText("Invalid password!");
        } else { // Closing the stage
        	Stage stage = (Stage) Butt.getScene().getWindow();
        	stage.close();
        }
	}
}
