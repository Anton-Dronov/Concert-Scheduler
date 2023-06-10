package application;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegaController{
	@FXML
	private PasswordField PSWD;
	@FXML
	private TextField UseThis;
	@FXML
	private TextField Telephone;
	@FXML
	private TextField Mailo;
	@FXML
	private Button Close;
	@FXML
	private Label Pointer;
	
	public Statement Connect() throws SQLException { // connecting to the database
		Statement stmt = DriverManager.getConnection("jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=Concert_Scheduler;integratedSecurity=true;encrypt=false").createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		return stmt;
	}
	
	public void Registrate(ActionEvent Event) throws NoSuchAlgorithmException {
		Boolean emptyUnameFlag = false; // is username is empty
		Boolean emptyEmailFlag = false; // is email is empty
		Boolean emptyPasswordFlag = false; // is password is empty
		
		String uName = UseThis.getText(); // the username text field
		if (uName.equals("")) { // is username field is empty?
			emptyUnameFlag = true;
		}
		
		String phone = Telephone.getText(); // the phone text field
		String male_07 = Mailo.getText(); // email text field
		if (male_07.equals("")) { // is email field is empty?
			emptyEmailFlag = true;
		}
		
		String tipak = "Client"; // this is unrealized feature to create different types of accounts (clients, cleaners, admins etc.)
		String password = PSWD.getText(); // the password text field
		if (password.equals("")) { // is password field is empty?
			emptyPasswordFlag = true;
		}
		
		if (emptyUnameFlag) {
			Pointer.setText("Enter the username!");
		} else if (emptyEmailFlag) {
			Pointer.setText("Enter the email!");
		} else if (emptyPasswordFlag) {
			Pointer.setText("Enter the password!");
		} else {
			
			/* Connecting the MD5 algorithm to cipher the password */
			MessageDigest md = MessageDigest.getInstance("MD5"); 
			byte[] messageDigest = md.digest(password.getBytes());
			BigInteger no = new BigInteger(1, messageDigest);
			String hashtext = no.toString(16);
			
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			
			try {
				user_full newUser = new user_full (uName, hashtext, phone, male_07, tipak);
				Connect().execute("INSERT INTO Client VALUES ('" + newUser.getUsername() + "', '" + newUser.getEmail() + "', '" + newUser.getPhone_number() + "', '" + newUser.getType() + "', '" + newUser.getPassword() + "');"); // registering new client
				new CurrentUser(uName, hashtext); // the current user is the registered one 
		
				/* Closing the current stage */
				Stage stage = (Stage) Close.getScene().getWindow();
				stage.close(); 
				
			} catch (SQLException SQL) { // SQLException is raised when we try to register an existing account
				Pointer.setText("This user has been already registered!");
			}
		}
	}
}
